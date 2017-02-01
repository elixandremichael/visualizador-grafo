/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizadorgrafo;

/**
 *
 * @author elixandre
 */
public class Busca {
    int noInicial;
    int[] vetorBusca;
    int[][] vetorCicloEuleriano;
    int iVetor;
    int nVertices;
    Aresta[][] adjacenciaBusca;
    boolean[] visitados;
    
    Busca(int n, Aresta[][] adja){
        vetorBusca = new int[n];
        visitados = new boolean[n];
        iVetor = 0;
        nVertices = n;   
        adjacenciaBusca = new Aresta[nVertices][nVertices];
        for(int i = 0; i<nVertices; i++){
            visitados[i] = false;
            vetorBusca[i] = -1;
            for(int j = 0; j<nVertices; j++){
                adjacenciaBusca[i][j] = new Aresta(adja[i][j].getRotulo(), adja[i][j].getCusto());
            }
        }
    }
    Busca(int n, Aresta[][] adja, int inicio){
        noInicial = inicio;
        vetorCicloEuleriano = new int[n][n];
        visitados = new boolean[n];
        iVetor = 0;
        nVertices = n;   
        adjacenciaBusca = new Aresta[nVertices][nVertices];
        for(int i = 0; i<nVertices; i++){
            visitados[i] = false;            
            for(int j = 0; j<nVertices; j++){
                adjacenciaBusca[i][j] = new Aresta(adja[i][j].getRotulo(), adja[i][j].getCusto());
            }
        }
    }
    void setNoInicial(int no){
        noInicial = no;
    }
    Aresta[][] getAdjacencia(){
        return adjacenciaBusca;
    }
    
    boolean[] getVisitados(){
        return visitados;
    }
    
    void setVisitados(boolean[] v){
        visitados = v;
    }
    
    boolean presenteVetorBusca(int x){
        for(int i = 0; i<nVertices; i++){
            if(vetorBusca[i] == x)
                return true;        
        }
        return false;       
    }
    
    int[] profundidade(int no){
        int visita = no;
        for(int i = 0; i<nVertices; i++){
            if(adjacenciaBusca[visita][i].getCusto() != -1 && i != visita && !visitados[i]){                                                        
                if(!presenteVetorBusca(visita))
                    vetorBusca[iVetor] = visita;
                iVetor++;
                visitados[visita] = true;                
                profundidade(i);
            }            
        }
        if(!visitados[visita])
            visitados[visita] = true;
        if(!presenteVetorBusca(visita))
            vetorBusca[iVetor] = visita;
        
        return vetorBusca;
    }
    
    protected boolean isGrafo(Aresta[][] grafo, int n){
        for(int i = 0; i<n; i++)
            for(int j = 0; j<n; j++)
                if(grafo[i][j].getCusto() != -1)
                    return true;
        return false;
    }
    
    boolean isConexo(){
        Aresta[][] adjacenciaGoodman = new Aresta[nVertices][nVertices];
        
        for(int i = 0; i<nVertices; i++){
            for(int j = 0; j<nVertices; j++){
                adjacenciaGoodman[i][j] = new Aresta(adjacenciaBusca[i][j].getRotulo(), adjacenciaBusca[i][j].getCusto());
            }        
        }
        
        int nComponentesConexo = 0, visita = 0, primeiro = 0;
        boolean[] visitados = new boolean[nVertices];
        boolean flag = false;
        
        while(isGrafo(adjacenciaGoodman, nVertices)){            
            Busca B = new Busca(nVertices, adjacenciaGoodman);
            for(int i = 0; i < nVertices; i++){//escolhe qualquer um que não foi visitado                
                if(!visitados[i]){
                    visita = i;
                    i = nVertices;
                }
                int conexos[] = B.profundidade(visita);                
                primeiro = conexos[0];
                flag = true;
                for(int j = 0; j < nVertices; j++){
                    if(conexos[j] != -1)
                        visitados[conexos[j]] = true;
                }
                for(int j = 1; j < nVertices; j++){
                    if(conexos[j] != -1){
                        for(int w = 0; w < nVertices; w++){
                            if(adjacenciaGoodman[primeiro][w].getCusto() < adjacenciaGoodman[conexos[j]][w].getCusto()){
                                adjacenciaGoodman[primeiro][w] = new Aresta(adjacenciaGoodman[conexos[j]][w].getRotulo(),adjacenciaGoodman[conexos[j]][w].getCusto());
                            }
                            adjacenciaGoodman[conexos[j]][w] = new Aresta("NULL", -1);
                        }
                    }
                }
            }
            nComponentesConexo++;
            if(flag){
                for(int i = 0; i<nVertices; i++)
                    adjacenciaGoodman[primeiro][i] = new Aresta("NULL", -1);
            }            
            flag = false;
        }
        for(int i = 0; i< nVertices; i++){
            if(!visitados[i])
                nComponentesConexo++;
        }        
        if(nComponentesConexo == 1){
            System.out.println("aqui:"+nComponentesConexo);
            return true;
        }
        return false;
    }
    int linhaVazia(){
        int cont = 0;
        for(int i = 0; i<nVertices; i++){
            for(int j = 0; j<nVertices; j++){
                if(adjacenciaBusca[i][j].getCusto()==-1)
                    cont++;
            }
            if(cont == nVertices)    
                return i;
            cont = 0;
        }
        return -1;
    }
    int[][] cicloEuleriano(int no){
        int visita = no;
        
        for(int i = 0; i<nVertices; i++){
            Aresta tmp;
            if(adjacenciaBusca[visita][i].getCusto() != -1 && i != visita /*&& !visitados[i]*/){                                                        
                tmp = new Aresta(adjacenciaBusca[visita][i].getRotulo(),adjacenciaBusca[visita][i].getCusto());
                adjacenciaBusca[visita][i] = new Aresta("NULL",-1);
                adjacenciaBusca[i][visita] = new Aresta("NULL",-1);
                int id = linhaVazia();
                if(id>=0){
                    nVertices--;
                    Aresta[][] tmp2 = adjacenciaBusca;
                    adjacenciaBusca = new Aresta[nVertices][nVertices];
                    for (int w=0; w<id;w++) // primeiro quadrante
                        for (int j=0; j<id; j++)
                            adjacenciaBusca[w][j] = tmp2[w][j];
                    for (int w=id+1; w<nVertices+1;w++) // segundo quadrante
                        for (int j=0; j<id; j++)
                            adjacenciaBusca[w-1][j] = tmp2[w][j];
                    for (int w=0; w<id;w++) // terceiro quadrante
                        for (int j=id+1; j<nVertices+1; j++)
                            adjacenciaBusca[w][j-1] = tmp2[w][j];
                    for (int w=id+1; w<nVertices+1;w++) // quarto quadrante
                        for (int j=id+1; j<nVertices+1; j++)
                            adjacenciaBusca[w-1][j-1] = tmp2[w][j];
                }
                if(isConexo()){
                    System.out.println("entrou");
                    vetorCicloEuleriano[iVetor][0] = visita;
                    vetorCicloEuleriano[iVetor][1] = i;

                    iVetor++;
                    /*visitados[visita] = true;*/
                    
                    cicloEuleriano(i);
                
                }else{
                    adjacenciaBusca[visita][i] = new Aresta(tmp.getRotulo(),tmp.getCusto());
                    adjacenciaBusca[i][visita] = new Aresta(tmp.getRotulo(),tmp.getCusto());
                }
            }            
        }
        //if(!visitados[visita])
        //    visitados[visita] = true;
        /*if(!presenteVetorBusca(visita)){
            vetorCicloEuleriano[iVetor][0] = visita;
            vetorCicloEuleriano[iVetor][1] = ;
        }*/
        for(int i =0; i<nVertices; i++)
            System.out.println(vetorCicloEuleriano[i][0]+" "+vetorCicloEuleriano[i][1]);
        System.out.println("");
        return vetorCicloEuleriano;
    }
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    void printAA(Aresta[][] adjacenciaGoodman, int nVerticesGoodman){
        for(int w = 0; w < nVerticesGoodman; w++){
            for(int y = 0; y < nVerticesGoodman; y++){
                if(adjacenciaGoodman[w][y].getCusto() != -1){
                    System.out.printf("1 ");
                }
                else
                    System.out.printf("0 ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
