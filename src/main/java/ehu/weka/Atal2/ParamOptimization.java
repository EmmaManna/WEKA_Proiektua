package ehu.weka.Atal2;

public class ParamOptimization {

    /** Egin beharrekoa: ParamOptimization ereduaren parametro optimoak ekortu
    *	@author Xabi Dermit, Jon Gondra eta Emma Manna
    **/

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println("Programaren helburua:");
            System.out.println("\tEreduaren parametro optimoak ekortu");
            System.out.println("\nAurrebaldintzak:");

            System.out.println("\nPost baldintza:");

            System.out.println("\nArgumentuen zerrenda eta deskribapena:");

            System.out.println("\t3- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar ParamOptimization.jar  ");
            System.exit(0);
        }
    }
}
