import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class schedule implements Comparable<schedule> {
    public int P;
    public int K;
    public int cost;
    Vector<element> elements;
    Vector<Vector<element>> r;

    public schedule(int p, int k) {
        this.P = p;
        this.K = k;
        r = new Vector<Vector<element>>(5 * P);
        for (int i = 0; i < 5 * p; i++) {
            Vector<element> v = new Vector<element>();
            r.add(v);
        }
    }

    public schedule(int p, int k,Vector<element> es) {
        this.P = p;
        this.K = k;
        r = new Vector<Vector<element>>(5 * P);
        for (int i = 0; i < 5 * p; i++) {
            Vector<element> v = new Vector<element>();
            r.add(v);
        }
        this.elements = es;
    }

    schedule(schedule s){
        P = s.P;
        K = s.K;
        cost = s.cost;
        r = s.r;
    }


    @Override
    public int compareTo(schedule o) {
        return this.cost - o.cost;
    }

    public void copy(schedule s,Vector<Vector<element>> elements){
        for(int i=0;i<elements.size();i++){
            for(int j=0;j<elements.elementAt(i).size();j++)
                s.r.elementAt(i).add(elements.elementAt(i).elementAt(j));
        }
    }

    public ArrayList<schedule> getSuccessors() {
        int count = 0;
        ArrayList<schedule> routine;
        routine = new ArrayList<schedule>();
        schedule temp= new schedule(P,K);

        for (int i = 0; i < r.size(); i++) {
            for (int j = 0; j < r.get(i).size(); j++) {
                for (int k = 1; k < r.size(); k++) {
                    if (!r.elementAt(k).contains(r.elementAt(i).elementAt(j))) {
                        //temp.r = r;
                        copy(temp,r);
                        temp.r.elementAt(k).add(r.elementAt(i).elementAt(j));
                        temp.r.get(i).remove(j);
                        count++;
                    }
                }
            }
            routine.add(temp);
            /*System.out.println(count);
            System.out.println(K);
            if(count>=K) return routine;*/
            if(routine.size()==K) return routine;
        }
        return routine;
    }
}
