package repositorio;

import interfaces.IRepositorioVenda;
import negocio.entidade.*;
import negocio.entidade.produto.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Esta classe realizar opera��es CRUD com objetos do tipo Venda.
 * @author �verton Vieira
 * @author Bruno Diniz
 * @version 2.00
 */
public class RepositorioVenda implements IRepositorioVenda, Serializable {
    
    private ArrayList<Venda> listaVendas;
    
    /**
     * Construtor RepositorioVenda
     */
    public RepositorioVenda() {
        this.listaVendas = new ArrayList<Venda>();
    }


    @Override
    public void adicionarVenda(Venda venda){
        this.listaVendas.add(venda);
    }
    

    @Override
    public double verLucroMensal(int mes){
        double lucroMensal = 0;
        for(int i = 0; i < this.listaVendas.size(); i++){
            if((this.listaVendas.get(i).getDataDeVenda().get(Calendar.MONTH) + 1)  == mes){
                lucroMensal += listaVendas.get(i).getValorTotal();
            }
        }
        return lucroMensal;
    }

    @Override
    public double verLucroAnual(int ano){
        double lucroAnual = 0;
        for (int i = 0; i < this.listaVendas.size(); i++) {
            if (this.listaVendas.get(i).getDataDeVenda().get(Calendar.YEAR) == ano) {
                lucroAnual += this.listaVendas.get(i).getValorTotal();
            }
        }
        return lucroAnual;
    }

    @Override
    public Funcionario determinarFuncionarioDoMes(int mes) {
        int quantidadeDeVendas = 0;
        int maiorQuantidadeDeVendas = 0;
        Funcionario funcDoMes = null;

        if(listaVendas.size() > 0) {

            for (int i = 0; i < listaVendas.size(); i++) {
                maiorQuantidadeDeVendas = 0;
                for (int j = 0; j < listaVendas.size(); j++) {
                    if (listaVendas.get(i).getFuncionario().equals(listaVendas.get(j).getFuncionario())
                            && (listaVendas.get(i).getDataDeVenda().get(Calendar.MONTH) + 1) == mes) {
                        maiorQuantidadeDeVendas++;
                    }
                }
                if (maiorQuantidadeDeVendas > quantidadeDeVendas) {
                    quantidadeDeVendas = maiorQuantidadeDeVendas;
                    funcDoMes = listaVendas.get(i).getFuncionario();
                }
            }
        }

        if (funcDoMes != null){
            funcDoMes.setFuncDoMes(true);
        }
        return funcDoMes;
    }

    @Override
    public Cliente determinarClienteFiel(int mes) {

        double valorGasto = 0;
        double maiorGastoEncontrado = 0;
        Cliente clienteFiel = null;

        for(int i = 0; i < listaVendas.size(); i++) {

            valorGasto = 0;

            for(int k = 0; k < listaVendas.size(); k++) {

                if(listaVendas.get(i).getCliente().equals(listaVendas.get(k).getCliente()) && listaVendas.get(i).getDataDeVenda().get(Calendar.MONTH) + 1 == mes) {
                    valorGasto += listaVendas.get(k).getValorTotal();
                }
            }

            if(valorGasto > maiorGastoEncontrado) {
                maiorGastoEncontrado = valorGasto;
                clienteFiel = listaVendas.get(i).getCliente();
            }
        }
        return clienteFiel;
    }

    public ArrayList<Venda> returnAllVendas(){
        ArrayList<Venda> retorno = new ArrayList<Venda>();
        for (Venda item:this.listaVendas) {
            retorno.add(item);
        }
        return retorno;
    }

    //M�todo auxiliar que busca os produtos que j� foram vendidos em alguma venda

    private ArrayList<Produto> buscarProdutosVendidos() {

        ArrayList<Produto> produtosVendidos = new ArrayList<Produto>(); // ArrayList de produtos que foram vendidos

        for(int i = 0; i < this.listaVendas.size(); i++) {

            for(int k = 0; k < this.listaVendas.get(i).getCarrinhoProdutos().size(); k++) {

                if(!produtosVendidos.contains(this.listaVendas.get(i).getCarrinhoProdutos().get(k))) {
                    produtosVendidos.add(this.listaVendas.get(i).getCarrinhoProdutos().get(k));
                }
            }
        }
        return produtosVendidos;
    }

    /**
     * Esse m�todo busca quais foram os produtos mais vendidos em determinado m�s.
     * @param mes
     * @return retorna um ArrayList de produtos
     */
    @Override
    public ArrayList<Produto> buscarProdutosMaisVendidos(int mes) {

        ArrayList<Produto> produtosVendidos = buscarProdutosVendidos();
        ArrayList<Produto> produtosMaisVendidos = new ArrayList<Produto>(); // Produtos mais vendidos do maior pro menor

        int vendido = 0; // Quantidade de vezes que o produto foi vendido
        int maisVendido = 0;
        Produto produtoAdicionado = null;

        // Numero de vezes que em que dever ser achado o produto mais vendido
        for(int i = 0; i < produtosVendidos.size(); i++) {

            maisVendido = 0;

            // Percorre o ArrayList de produtos que foram vendidos
            for(int l = 0; l < produtosVendidos.size(); l++) {

                vendido = 0;

                // Percorre ArrayList de vendas(listaVendas)
                for(int k = 0; k < this.listaVendas.size(); k++) {

                    // Percorre o ArrayList de produtos(carrinhoProdutos) da venda(listaVendas.get(i)) em questao
                    for(int j = 0; j < this.listaVendas.get(k).getCarrinhoProdutos().size(); j++) {

                        // Se o produto vendido estiver no carrinhosProdutos de tal venda E o produto ainda nao esta no array de produtos mais vendidos
                        if(produtosVendidos.get(l).equals(this.listaVendas.get(k).getCarrinhoProdutos().get(j))
                                && !produtosMaisVendidos.contains(produtosVendidos.get(l))
                                && this.listaVendas.get(k).getDataDeVenda().get(Calendar.MONTH) + 1 == mes) {
                            vendido++;
                        }

                        if(vendido >= maisVendido) {
                            maisVendido = vendido;
                            produtoAdicionado = produtosVendidos.get(l);
                        }
                    }
                }
            }

            if(!produtosMaisVendidos.contains(produtoAdicionado)) {
                produtosMaisVendidos.add(produtoAdicionado);
            }

        }
        return produtosMaisVendidos;
    }


    public ArrayList<Cliente> buscarClientesComPendencia() {

        ArrayList<Cliente> clientesPendentes = new ArrayList<Cliente>();

        for(int i = 0; i < listaVendas.size(); i++) {

            if(listaVendas.get(i).getPagamenteParcelado() == true) {
                clientesPendentes.add(listaVendas.get(i).getCliente());
            }
        }
        return clientesPendentes;
    }

    @Override
    public void salvarDados() {
        try{
            FileOutputStream file = new FileOutputStream("listaVendas.dat");
            ObjectOutputStream os = new ObjectOutputStream(file);
            os.writeObject(this.listaVendas);
            os.close();
        }catch(IOException ioException){

        }

    }

    @Override
    public void lerDados(){
        try{
            FileInputStream file = new FileInputStream("listaVendas.dat");
            ObjectInputStream is = new ObjectInputStream(file);
            ArrayList<Venda> listaVendas = (ArrayList<Venda>) is.readObject();
            this.listaVendas = listaVendas;
            is.close();

        } catch (FileNotFoundException fileNotFound) {

        } catch (IOException ioException) {

        } catch (ClassNotFoundException classNotFound) {

        }
    }
}
