import java.util.Random;
import java.util.Scanner;
import java.io.IOException;

public class The8QueensPuzzle{
    //poblacion inicial
    //fitness
    //crossover
    //mutacion  REPETIR
    public The8QueensPuzzle(){
    }
    //----------------------------------------------------------------------
    public static void main(String[]args){
        int N=4;
        Poblacion poblacion = new Poblacion(N);
        //Integer[][] cromosomas= new Integer[N][8];
        //asignar elementos de modo que no ese repitan
        //------------------------------------------------------------------
        Random rand = new Random(); 
        for(int i=0;i<poblacion.cromosomas.length;i++){
            poblacion.cromosomas[i].indOrden = i;
            poblacion.cromosomas[i].genes[0] = rand.nextInt(8)+1;
            for(int j=1;j<poblacion.cromosomas[0].genes.length;j++){
                int num= -1;
                //corroborar que no se repita
                int posRep = -1;
                do{
                    //System.out.println("dentro de do ,posResp="+posRep);
                    num = rand.nextInt(8)+1;
                    //busqueda en los elementos ver si se REPITE
                    int k =0;
                    for(;k<j;k++){   
                        if(num == poblacion.cromosomas[i].genes[k]){
                            posRep = k;
                            break;
                        }
                    }if(k==j){
                        posRep = -1;
                    }
                }while(posRep!=-1);
                poblacion.cromosomas[i].genes[j] = num;
            }
        }
        //--------------------------------------------------------------------------
        //imprimiendo la poblacion inicial 
        for(int i=0;i<poblacion.cromosomas.length;i++){
            for(int j=0;j<poblacion.cromosomas[0].genes.length;j++){
                System.out.printf("%d\t",poblacion.cromosomas[i].genes[j]);
            }
            System.out.println();
        }
       //----------------WHILE-------------------(acabar si fitnes de algun cromosoma es 1)
       boolean flagGlobal = false;
       int contadorGlobal =0;
       while(!flagGlobal&&(contadorGlobal<5)){
             //funcion aptitud fitness INICIO
        //------------------------------------------------------------------------
        poblacion.CalcularFitness();
        // ----fucion aptitud fitness FIN
        //-----------------------------------------------------------------------
        for(int i=0;i<poblacion.cromosomas.length;i++){
            System.out.printf("cromosoma = %d fitnes = %1.5f\n",(poblacion.cromosomas[i].indOrden+1),poblacion.cromosomas[i].fitness);
        }
        //----SELECCION---------------------------------------------------
        //---calculo de probabilidades----
        poblacion.CalcularProbabilidades();
        for(int i=0;i<poblacion.cromosomas.length;i++){
            System.out.printf("cromosoma = %d probabilidad = %1.5f\n",(poblacion.cromosomas[i].indOrden+1),poblacion.cromosomas[i].probabilidad);
        }
        poblacion.CrearRangos();
        for(int i=0;i<poblacion.cromosomas.length;i++){
            System.out.printf("[%1.5f,%1.5f)\n",poblacion.cromosomas[i].rango.lInferior,poblacion.cromosomas[i].rango.lSuperior);
        }
        //--------SELECCION---------------------generar el numero aleatorio para escoger al par que van a hacer el crossover
        Random random=new Random();
        double variableCross;
        //si variableCross esta en alguno de los intervalos
        int [] indiceElegidos = new int[2];
        int cont = 0;
        while(cont < indiceElegidos.length){
            variableCross = random.nextDouble();
            for(int i=0;i<poblacion.cromosomas.length;i++){
                if(poblacion.cromosomas[i].rango.pertenece(variableCross)){
                    if(poblacion.cromosomas[i].rango.idRango!=((i==1)?indiceElegidos[i-1]:-1)){   
                        System.out.printf(" idRango: %d variableCross: %1.5f\n",poblacion.cromosomas[i].rango.idRango,variableCross);        //ind actual diferente del anterior)
                        indiceElegidos[cont] = poblacion.cromosomas[i].rango.idRango;
                        cont++;
                        break;
                    }else{
                        cont = cont ;
                        break;
                    }
                }
            }
        }
        //imprimiendo los elegidos
         System.out.println("padres");
        for(int i=0;i<indiceElegidos.length;i++){
            for(int j=0;j<poblacion.cromosomas[0].genes.length;j++){
                System.out.printf("%d\t",poblacion.cromosomas[indiceElegidos[i]].genes[j]);
            }
            System.out.printf("probabilidad: %1.5f\n",poblacion.cromosomas[indiceElegidos[i]].probabilidad);
        }
        //------------------------------------CROSSOVER------------------realizar el cross
        int nHijos = 2;
        Cromosoma[] hijos = new Cromosoma[nHijos]; 
        for(int i = 0;i<hijos.length;i++){
            hijos[i] = new Cromosoma();
            for(int j=0;j<hijos[i].genes.length;j++){
                //System.out.println(j>3?(int)Math.abs(i-1):i);
                hijos[i].genes[j] = poblacion.cromosomas[indiceElegidos[j>3?(int)Math.abs(i-1):i]].genes[j];
            }
        }
        //imprimiendo cromosomasHijos
        System.out.println("hijos cross");
        for(int i=0;i<hijos.length;i++){
            for(int j=0;j<hijos[i].genes.length;j++){
                System.out.printf("%d\t",hijos[i].genes[j]);
            }
            System.out.println();
        }
        //corregir si hay repetidos
        int[]cuenta;
        int[]arr; 
        for(int i = 0;i<hijos.length;i++){
            //que genes faltan
            arr = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
            cuenta=new int[]{0,0,0,0,0,0,0,0};
            for(int k=0;k<cuenta.length;k++){
                for(int j=0;j<hijos[i].genes.length;j++){
                    if((k+1)==hijos[i].genes[j]){
                        //aumenta si el elemento esta
                        arr[k]=j; //el indice ultimo donde se repite
                        cuenta[k]++;    //para el repetido  2
                    }
                }
            }
            //cuales se repiten
            for(int k=0;k<arr.length;k++){
                //si no esta en los genes
                if(arr[k]==-1){
                    //determinar el que se repite
                    for(int j=0;j<cuenta.length;j++){
                        if(cuenta[j]==2){
                            hijos[i].genes[arr[j]] = k+1;
                            break;
                        }
                    }
                }
            }
        }
        //imprimiendo cromosomasHijos
        System.out.println("hijos cross corregidos");
        for(int i=0;i<hijos.length;i++){
            for(int j=0;j<hijos[i].genes.length;j++){
                System.out.printf("%d\t",hijos[i].genes[j]);
            }
            System.out.println();
        }
        //-----------------------MUTACION----------------------------
        //-----mutar(intercambiar posicion de genes)-----------------
        
        if(contadorGlobal%3==0){
            for(int i=0;i<hijos.length;i++){
                int ind1 = rand.nextInt(8);
                int ind2 = (int)(Math.abs(ind1 -1));
                int temp =hijos[i].genes[ind1];
                hijos[i].genes[ind1] = hijos[i].genes[ind2];
                hijos[i].genes[ind2] = temp; 
            }
            //----------------------------------------------------
            System.out.println("hijos mutados");
            for(int i=0;i<hijos.length;i++){
                for(int j=0;j<hijos[i].genes.length;j++){
                    System.out.printf("%d\t",hijos[i].genes[j]);
                }
                System.out.println();
            }
        }
        //---------------------------------------------------------
        poblacion.ImprimirCromosomas();
        //reemplazando los 2 mas bajos
        poblacion.CalcularFitness(hijos[0]);
        poblacion.CalcularFitness(hijos[1]);
        poblacion.InsertarHijos(hijos);
        poblacion.ImprimirCromosomas();
        for(int i =0;i<poblacion.cromosomas.length;i++){
            flagGlobal =(poblacion.cromosomas[i].fitness ==1);
        }
        contadorGlobal ++;
       }//fin While Global
    } //fin main
    //---------------------------------------------------------------------------------
}
//===============================================================================
class Poblacion{
    static Cromosoma [] cromosomas ;
    static double probabilidades[];
    static Cromosoma elite;
    //-----------------------------------------------------------------------------
    Poblacion(int N){
        cromosomas = new Cromosoma[N];
        for(int i=0;i<N;i++){
            cromosomas[i] = new Cromosoma();//creando los cromosomas
            //cromosomas[i].genes = new int[8]; ya se hace en el constructor de Cromosoma2
        }
        probabilidades = new double[N];
        elite = new Cromosoma();
    }
    //-------------------------------------------------------------------------
    public static void OrdenarIndices(){
        //reemplazar cromosomas segun fitnes
        for(int i=0;i<cromosomas.length;i++){
            for(int j=0;j<(cromosomas.length-i-1);j++){
                if(cromosomas[j].fitness>cromosomas[j+1].fitness){
                    Cromosoma temp =new Cromosoma();
                    temp = cromosomas[j];
                    cromosomas[j] =cromosomas[j+1];
                    cromosomas[j+1] = temp;
                }
            }
        }
        for(int i=0;i<cromosomas.length;i++){
            cromosomas[i].indOrden = i;
        }
        for(int i=0;i<cromosomas.length;i++){
            cromosomas[i].rango.idRango = i;
        }
    }//---------------------------------------------------------------------------
    public static void CalcularFitness(Cromosoma cromosoma){
         //C[i] - i para la ascendente       C[i]+i para para la descendente
         int conflictos = 0;
         for(int j=0;j<cromosoma.genes.length;j++){
             //luego del elemento
             for(int k=0;k<cromosoma.genes.length;k++){
                 //si conflicto  conflictos++;
                if(k!=j){
                     if((cromosoma.genes[k]-(k+1) )==(cromosoma.genes[j]-(j+1))){
                         //System.out.printf("ascendente:(%d,%d)~(%d,%d)\n",cromosoma.genes[k],(k+1),cromosoma.genes[j],(j+1));
                         conflictos++;
                     }
                     if((cromosoma.genes[k]+(k+1))==(cromosoma.genes[j]+(j+1))){
                         //System.out.printf("descendente:(%d,%d)~(%d,%d)\n",cromosoma.genes[k],(k+1),cromosoma.genes[j],(j+1));
                         conflictos++;
                     }
                }
             }
              
         }
         //System.out.printf("conflictos cromosoma %d :%d\n",(cromosoma.indOrden+1),conflictos);
         double factor =Math.pow(10,5);
         cromosoma.fitness = Math.round(factor/(1+conflictos))/factor;
         System.out.println();
    }
    //---------------------------------------------------------------------------
public static void CalcularFitness(){
    for(int i=0;i<cromosomas.length;i++){
            //C[i] - i para la ascendente       C[i]+i para para la descendente
         String [][] tabla = new String[8][8];
         for(int f=0;f<8;f++){
             for(int c=0;c<8;c++){
                 tabla[f][c] = "| |";
             }
         }
         for(int l=0;l<cromosomas[i].genes.length;l++){
             tabla[cromosomas[i].genes[l]-1][l]="|*|";
         }
         System.out.println();
         for(int f=0;f<8;f++){
             for(int c=0;c<8;c++){
                 System.out.print(tabla[f][c]);
             }
             System.out.println();
         }
         int conflictos = 0;
         for(int j=0;j<cromosomas[0].genes.length;j++){
             //luego del elemento
             for(int k=0;k<cromosomas[i].genes.length;k++){
                 //si conflicto  conflictos++;
                if(k!=j){
                     if((cromosomas[i].genes[k]-(k+1) )==(cromosomas[i].genes[j]-(j+1))){
                         System.out.printf("ascendente:(%d,%d)~(%d,%d)\n",cromosomas[i].genes[k],(k+1),cromosomas[i].genes[j],(j+1));
                         conflictos++;
                     }
                     if((cromosomas[i].genes[k]+(k+1))==(cromosomas[i].genes[j]+(j+1))){
                         System.out.printf("descendente:(%d,%d)~(%d,%d)\n",cromosomas[i].genes[k],(k+1),cromosomas[i].genes[j],(j+1));
                         conflictos++;
                     }
                }
             }
         }
         System.out.printf("conflictos cromosoma %d :%d\n",(cromosomas[i].indOrden+1),conflictos);
         double factor =Math.pow(10,5);
         cromosomas[i].fitness = Math.round(factor/(1+conflictos))/factor;
         System.out.println();
         }
         CalcularProbabilidades(); // calcula las probabilidades
         EstablecerElite(); // establece el elite

    }
    //------------------------------------------------------------------------------
    public static void EstablecerElite(){
        double mayorFitness = elite.fitness;
        int indMayor = -1;
        for(int i=0;i<cromosomas.length;i++){
            if(cromosomas[i].fitness>mayorFitness){
                mayorFitness = cromosomas[i].fitness;
                indMayor = i;
            }
        }
        if(indMayor>-1){
            elite = new Cromosoma(cromosomas[indMayor]); 
        }

    }
    //---------------------------------------------------------------------------
    public static double FitnessTotal(){
        double fitnessTotal = 0.0;
        for(int i=0;i<cromosomas.length;i++){
            fitnessTotal+=cromosomas[i].fitness;
        }
        return fitnessTotal;
    }
    //---------------------------------------------------------------------------
    public static void CalcularProbabilidades(){
        double fitnesTotal = FitnessTotal(); 
        for(int i=0;i<(probabilidades.length)&&(i<cromosomas.length);i++){
            //probabilidades[i] = cromo58somas[i].fitness/fitnesTotal;
            cromosomas[i].probabilidad = cromosomas[i].fitness/fitnesTotal;
        }
    }
    //------------------------------------------------------------------
    public static void CrearRangos(){
        double acumulada = 0;
        for(int i = 0;i<cromosomas.length;i++){
            cromosomas[i].rango =new Rango(cromosomas[i].indOrden, acumulada, acumulada + cromosomas[i].probabilidad);
            acumulada +=cromosomas[i].probabilidad;
        }
    }
    //------------------------------------------------------------------
    public static void ImprimirCromosomas(){
        System.out.println();
        for(int i= 0;i<cromosomas.length;i++){
            for(int j=0;j<cromosomas[i].genes.length;j++){
                System.out.printf("%d\t",cromosomas[i].genes[j]);
            }
            System.out.printf("idOrden: %d ,fitnes: %1.5f  \n",cromosomas[i].indOrden,cromosomas[i].fitness);
        }
        System.out.println();
    }
    //---------------------------------------------------------------------------------------------
    public static void InsertarHijos(Cromosoma[] cromos){
        Random rand = new Random();
        int ind1 = rand.nextInt(cromosomas.length);
        int ind2 = (int)(Math.abs(ind1 -1));
        cromosomas[ind1] = new Cromosoma(cromos[0]);
        cromosomas[ind2] = new Cromosoma(cromos[1]);
        //corregir los indices
        for(int i = 0;i<cromosomas.length;i++){
            cromosomas[i].indOrden = i;
        } 
    }
}
//============================================================================
class Cromosoma{
    int[] genes;
    int indOrden;
    double fitness;
    double probabilidad;
    Rango rango;
    Cromosoma(){
        genes = new int[8];
    }
    Cromosoma(Cromosoma otro) {
        this.genes = otro.genes.clone(); // Copia profunda de los genes
        this.indOrden = otro.indOrden;
        this.fitness = otro.fitness;
        this.probabilidad = otro.probabilidad;
        if (otro.rango != null) {
            this.rango = new Rango(otro.rango.idRango, otro.rango.lInferior, otro.rango.lSuperior);
        }
    }
}
//===================================================================================================
class Rango{
    double lSuperior;
    double lInferior;
    int idRango;
    Rango(int id, double inf, double sup){
        this.idRango = id;
        this.lInferior = inf;
        this.lSuperior = sup;
    }
    public boolean pertenece(double num){
        if((num>=lInferior) && (num<lSuperior)){
            return true;
        }
        return false;
    } 
}

