import java.util.List;
import java.util.Queue;

class Outer<T1,T2>
{
    class Inner<I1,I2,I3>
    {
        void foo() {
            Outer<String,List<String>> o1 = new Outer<String,List<String>>();
            Outer<String,List<String>> o2 = new Outer<String,List<String>>("hi", "bye");
            o1.bar();
            o2.baz(0, "hi", "bye");
        }
    }

    Outer() {
    }

    <U1> Outer(String s, U1 u) {
    }

    void bar()
    {
        Queue<String> q;
    }

    <U2> void baz(int i, U2 u, String s)
    {
    }

    private void gee(Queue<String> b)
    {
    }

    private void gaz(Queue<List<String>> a)
    {
    }
}
