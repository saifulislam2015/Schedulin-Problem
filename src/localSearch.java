import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class localSearch {
    public static int P,T,R,C,K;
    public static Vector<schedule> initStates = new Vector<schedule>();

    public  localSearch(int P,int t,int r,int c,int k,Vector<schedule> e){
        localSearch.P = P;
        T = t;
        R = r;
        C = c;
        K = k;
        initStates = e;
    }


    public int CostFunction(schedule s){
        int cost = 0;
        int w1=1;
        int w2 =1 ;
        int w3 = 1;

        //Iterator it = s.r.iterator();
        for(Vector<element> p:s.r){

            int tc = TeacherConflict(p,w1);
            //System.out.println("tc"+tc);
            cost+=tc;
            //System.out.println(cost);
            int rc = RoomConflict(p,w2);
            //System.out.println("rc"+rc);
            cost+=rc;
            //System.out.println(cost);
            int cc = ClassConflict(p,w3);
            //System.out.println("cc"+cc);
            cost+=cc;
            //System.out.println(cost);
        }
        return cost;
    }

    public int TeacherConflict(Vector<element> p,int w){

        int total = 0;
        for(int i=0;i<T;i++){
            int count = 0;
            for(int j=0;j<p.size();j++){
                if(p.elementAt(j).T==i) count++;
            }
            total+=Math.max(0,count-1);
        }
        return w*total;
    }

    public int RoomConflict(Vector<element> p,int w){

        int total = 0;
        for(int i=0;i<R;i++){
            int count = 0;
            for(int j=0;j<p.size();j++){
                if(p.elementAt(j).R==i) count++;
            }
            total+=Math.max(0,count-1);
        }
        return w*total;
    }

    public int ClassConflict(Vector<element> p,int w){

        int total = 0;
        for(int i=0;i<C;i++){
            int count = 0;
            for(int j=0;j<p.size();j++){
                if(p.elementAt(j).C==i) count++;
            }
            total+=Math.max(0,count-1);
        }
        return w*total;
    }


    /*public schedule Search(){

        while (true){
            ArrayList<schedule> pq = new ArrayList<schedule>();
            for(int i=0;i<K;i++) {
                pq.addAll(initStates.elementAt(i).getSuccessors());
            }

                schedule best = pq.remove(0);
                if(CostFunction(initStates.elementAt(0))<CostFunction(best)){
                    return initStates.elementAt(0);
                }
                initStates.add(0,best);

                for(int j=1;j<K;j++){
                    initStates.add(j,pq.remove(0));
                }


        }
    }*/



    public schedule Search(){

        int q = 0,p=0;
        while (true){
            ArrayList<schedule> pq = new ArrayList<schedule>();
            q++;
            for(int i=0;i<K;i++){
                p++;
                pq.addAll(initStates.elementAt(i).getSuccessors());

                schedule best = pq.remove(0);
                if(CostFunction(initStates.elementAt(0))<CostFunction(best)){
                    //System.out.println(q);
                    //System.out.println(p);
                    return initStates.elementAt(0);
                }
                initStates.add(0,best);

                for(int j=1;j<K;j++){
                    Random rand = new Random();
                    int idx = rand.nextInt(Integer.MAX_VALUE)%K;
                    initStates.add(j,pq.remove(idx));
                }

            }

        }
    }

    public schedule StochasticSearch(){


        while (true){
            ArrayList<schedule> pq = new ArrayList<schedule>();
            ArrayList<schedule> L = new ArrayList<schedule>();

            for(int i=0;i<K;i++){
                pq.addAll(initStates.elementAt(i).getSuccessors());

                for(int j=0;j<pq.size();j++){
                    int c = CostFunction(pq.get(j));
                    pq.get(j).cost = c;
                }
                Collections.sort(pq);

                int maxCost = pq.get(pq.size()-1).cost;

                for(int j=0;j<pq.size();j++){
                    int n = (maxCost+1)-pq.get(j).cost;

                    for(int k=0;k<n;k++){
                        L.add(pq.get(j));
                    }
                }

                schedule best = L.remove(0);
                if(CostFunction(initStates.elementAt(0))<CostFunction(best)){
                    return initStates.elementAt(0);
                }
                initStates.add(0,best);

                for(int j=1;j<K;j++){
                    initStates.add(j,L.remove(0));
                }

            }

        }
    }




    public static void main(String[] args) {

        int[] in = new int[5];
        int index = 0;
        //T S C R Re
        try {
            Scanner input = new Scanner(new File("hdtt4note.txt"));

            while (input.hasNextInt()){
                //String s = input.next();
                int s = input.nextInt();
                in[index] = s;
                index++;
                //System.out.println(s);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        int T = in[0];
        int C = in[2];
        int R = in[3];
        int P = 6 ;
        int K = 5;
        Vector<schedule> inits = new Vector<schedule>() ;
        /*Vector<element> elements = new Vector<element>();
        for(int i=0;i<K;i++){
            //schedule s = new schedule(P,K);
            schedule s = new schedule(P,K,elements);
            init.add(s);
        }*/
        schedule init = new schedule(P,K);

        try {
            Scanner input = new Scanner(new File("hdtt4req.txt"));

            while (input.hasNextInt()){
                for(int i=0;i<R; i++){
                    for(int j=0;j<T;j++){
                        for(int k=0;k<C;k++){

                            element e = new element(j,k,i,input.nextInt());
                            //elements.add(e);

                            /*for(int l=0;l<K;l++){
                                Random rand = new Random();
                                int idx = rand.nextInt(Integer.MAX_VALUE)%(5*P);
                                init.elementAt(l).r.elementAt(idx).add(e);
                            }*/
                            Random rand = new Random();
                            int idx = rand.nextInt(Integer.MAX_VALUE)%(5*P);
                            init.r.elementAt(idx).add(e);
                        }
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        inits.add(init);



        localSearch beam = new localSearch(P,T,R,C,K,inits);
        schedule soln = beam.StochasticSearch();
        //schedule soln = beam.StochasticSearch();
        System.out.println("res: "+ beam.CostFunction(soln));


    }

}
