import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class SumInLeavesVisitor extends TreeVis {
    private List<Tree> list = new ArrayList<>();

    public int getResult() {
        int sum = 0;
        for (Tree tree : this.list) {
            sum += tree.getValue();
        }
        return sum;
    }

    public void visitNode(TreeNode node) {
        //no implementations required for Nodes
    }

    public void visitLeaf(TreeLeaf leaf) {
        this.list.add(leaf);
    }
}

class ProductOfRedNodesVisitor extends TreeVis {
    private List<Tree> list = new ArrayList<>();
    static final int MODULO = 10^9 + 7;

    public int getResult() {
        int mod = 1;
        for (Tree tree : this.list) {
            mod = mod * (tree.getValue() % MODULO);
            System.out.println("mod:" + mod);
        }
        return mod;
    }

    public void visitNode(TreeNode node) {
        if (node.getColor() == Color.RED) {
            this.list.add(node);
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.RED) {
            this.list.add(leaf);
        }
    }
}

class FancyVisitor extends TreeVis {
    private List<Tree> list = new ArrayList<>();

    public int getResult() {
        int sum1 = 0;
        int sum2 = 0;
        for (Tree tree : this.list) {
            if (tree instanceof TreeNode) sum1 += tree.getValue();
            if (tree instanceof TreeLeaf) sum2 += tree.getValue();
        }

        return Math.abs(sum2 - sum1);
    }

    public void visitNode(TreeNode node) {
        if (node.getDepth() % 2 == 0) {
            this.list.add(node);
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.GREEN) {
            this.list.add(leaf);
        }
    }
}

public class Solution {

    public static void main(String[] args) throws Exception {
        visitorExercise();
    }

    public static Tree solve() {
        //read the tree from STDIN and return its root as a return value of this function
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();
        int [] values = new int[n];
        int i;
        for (i = 0; i < n; i++) {
            values[i] = scan.nextInt();
        }
        Color [] colors = new Color[n];
        for (i = 0; i < n; i++) {
            int x = scan.nextInt();
            if (x == 0) colors[i] = Color.RED;
            else colors[i] = Color.GREEN;
        }
        int [][] arr = new int[n - 1][2];
        for (i = 0; i < n - 1; i++) {
            arr[i][0] = scan.nextInt();
            arr[i][1] = scan.nextInt();
        }
        Arrays.sort(arr, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });

        TreeNode root = null;
        Map<Integer, Tree> map = new HashMap<>();
        for (i = 0; i < n; i++) {
            int value = values[i];
            Color color = colors[i];
            if (i == 0) {
                root = new TreeNode(value, color, 0);
                map.put(1, root);
            } else {
                int nodeFrom = arr[i - 1][0];
                int nodeTo = arr[i - 1][1];
                System.out.println("nodeFrom:" + nodeFrom + ",nodeTo:" + nodeTo);
                TreeNode parentTreeNode = (TreeNode) map.get(nodeFrom);
                int depth = parentTreeNode.getDepth() + 1;
                if (isNode(nodeTo, arr)) {
                    TreeNode treeNode = new TreeNode(value, color, depth);
                    map.put(nodeTo, treeNode);
                    parentTreeNode.addChild(treeNode);
                } else {
                    TreeLeaf treeLeaf = new TreeLeaf(value, color, depth);
                    map.put(nodeTo, treeLeaf);
                    parentTreeNode.addChild(treeLeaf);
                }
            }
        }
        return root;
    }

    private static boolean isNode(int to, int [][] arr) {
        boolean isNode = false;
        for (int[] ints : arr) {
            int fromNode = ints[0];
            if (to == fromNode) {
                isNode = true;
                break;
            }
        }
        return isNode;
    }

    private static void visitorExercise() {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }

    private static void primeExercise() {
        Scanner scanner = new Scanner(System.in);
        String n = scanner.nextLine();
        System.out.println(new BigInteger(n).isProbablePrime(2) ? "prime" : "not prime");
        scanner.close();
    }

    private static void bigIntegerExercise() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        BigInteger num1 = new BigInteger(s);
        s = in.next();
        BigInteger num2 = new BigInteger(s);
        System.out.println(num1.add(num2).toString());
        System.out.println(num1.multiply(num2).toString());
    }

    private static void sha256exercise() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        System.out.println(s);
        byte [] bytes = s.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(bytes);
            StringBuilder buffer = new StringBuilder();
            System.out.println("count:" + out.length);
            for (byte b : out) {
                System.out.println("b:" + String.valueOf(b) + ",h:" + String.format("%02x", b & 0xff));
                buffer.append(String.format("%02x", b & 0xff));
            }
            System.out.println(buffer.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void md5exercise() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        System.out.println(s);
        byte [] bytes = s.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] out = md.digest(bytes);
            StringBuilder buffer = new StringBuilder();
            System.out.println("count:" + out.length);
            for (byte b : out) {
                System.out.println("b:" + String.valueOf(b) + ",h:" + String.format("%02x", b & 0xff));
                buffer.append(String.format("%02x", b & 0xff));
            }
            System.out.println(buffer.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void lambadaExercise()  throws IOException {
        MyMath ob = new MyMath();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        PerformOperation op;
        boolean ret = false;
        String ans = null;
        while (T-- > 0) {
            String s = br.readLine().trim();
            StringTokenizer st = new StringTokenizer(s);
            int ch = Integer.parseInt(st.nextToken());
            int num = Integer.parseInt(st.nextToken());
            if (ch == 1) {
                op = ob.isOdd();
                ret = ob.checker(op, num);
                ans = (ret) ? "ODD" : "EVEN";
            } else if (ch == 2) {
                op = ob.isPrime();
                ret = ob.checker(op, num);
                ans = (ret) ? "PRIME" : "COMPOSITE";
            } else if (ch == 3) {
                op = ob.isPalindrome();
                ret = ob.checker(op, num);
                ans = (ret) ? "PALINDROME" : "NOT PALINDROME";

            }
            System.out.println(ans);
        }
    }

    private static void annotationExercise() {
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());
        while (testCases > 0) {
            String role = in.next();
            int spend = in.nextInt();
            try {
                Class annotatedClass = FamilyMember.class;
                Method[] methods = annotatedClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(FamilyBudget.class)) {
                        FamilyBudget family = method
                                .getAnnotation(FamilyBudget.class);
                        String userRole = family.userRole();
                        int budgetLimit = family.budgetLimit();
                        if (userRole.equals(role)) {
                            if(spend <= budgetLimit){
                                method.invoke(FamilyMember.class.newInstance(),
                                        budgetLimit, spend);
                            }else{
                                System.out.println("Budget Limit Over");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            testCases--;
        }
    }

    private static void varargsTest() {
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            int n1=Integer.parseInt(br.readLine());
            int n2=Integer.parseInt(br.readLine());
            int n3=Integer.parseInt(br.readLine());
            int n4=Integer.parseInt(br.readLine());
            int n5=Integer.parseInt(br.readLine());
            int n6=Integer.parseInt(br.readLine());
            Add ob=new Add();
            ob.add(n1,n2);
            ob.add(n1,n2,n3);
            ob.add(n1,n2,n3,n4,n5);
            ob.add(n1,n2,n3,n4,n5,n6);
            Method[] methods=Add.class.getDeclaredMethods();
            Set<String> set=new HashSet<>();
            boolean overload=false;
            for(int i=0;i<methods.length;i++)
            {
                if(set.contains(methods[i].getName()))
                {
                    overload=true;
                    break;
                }
                set.add(methods[i].getName());

            }
            if(overload)
            {
                throw new Exception("Overloading not allowed");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void exceptionExercise2() {
        Scanner in = new Scanner(System.in);
        MyCalculator my_calculator = new MyCalculator();

        while (in .hasNextInt()) {
            int n = in.nextInt();
            int p = in.nextInt();

            try {
                System.out.println(my_calculator.power(n, p));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void exceptionExercise() {
        Scanner scan = new Scanner(System.in);
        try {
            int x = Integer.parseInt(scan.nextLine());
            int y = Integer.parseInt(scan.nextLine());
            System.out.println(x/y);
        } catch (NumberFormatException nex) {
            System.out.println(InputMismatchException.class.getCanonicalName());
        } catch (ArithmeticException aex) {
            System.out.println(aex.getClass().getCanonicalName() + ": " + aex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getClass().getCanonicalName());
        }
    }

    private static void javaPriorityQueueExercise() {
        Scanner scan = new Scanner(System.in);
        Priorities priorities = new Priorities();
        int totalEvents = Integer.parseInt(scan.nextLine());
        List<String> events = new ArrayList<>();

        while (totalEvents-- != 0) {
            String event = scan.nextLine();
            events.add(event);
        }

        List<Student> students = priorities.getStudents(events);

        if (students.isEmpty()) {
            System.out.println("EMPTY");
        } else {
            for (Student st: students) {
                System.out.println(st.getName());
            }
        }
    }

    private static void javaSortExercise() {
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());

        List<Student> studentList = new ArrayList<>();
        while(testCases > 0) {
            int id = in.nextInt();
            String fname = in.next();
            double cgpa = in.nextDouble();

            Student st = new Student(id, fname, cgpa);
            studentList.add(st);

            testCases--;
        }
        studentList.sort((o1, o2) ->
                o1.getCgpa() == o2.getCgpa() ? (
                        o1.getFname().equals(o2.getFname()) ?
                                o1.getId() - o2.getId() : o1.getFname().compareTo(o2.getFname())) :
                        (int) Math.signum(o2.getCgpa() - o1.getCgpa())
                );
        for(Student st: studentList){
            System.out.println(st.getFname());
        }
    }

    private static void comparatorExercise() {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        Player[] player = new Player[n];
        Checker checker = new Checker();

        for(int i = 0; i < n; i++){
            player[i] = new Player(scan.next(), scan.nextInt());
        }
        scan.close();

        Arrays.sort(player, checker);
        for(int i = 0; i < player.length; i++){
            System.out.printf("%s %s\n", player[i].name, player[i].score);
        }
    }

    private static void genericExercise() {
        Printer myPrinter = new Printer();
        Integer[] intArray = { 1, 2, 3 };
        String[] stringArray = {"Hello", "World"};
        myPrinter.printArray(intArray);
        myPrinter.printArray(stringArray);
        int count = 0;

        for (Method method : Printer.class.getDeclaredMethods()) {
            String name = method.getName();

            if(name.equals("printArray"))
                count++;
        }

        if(count > 1)System.out.println("Method overloading is not allowed!");
    }

    private static void setUniqueSet() {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        String [] pair_left = new String[t];
        String [] pair_right = new String[t];

        for (int i = 0; i < t; i++) {
            pair_left[i] = s.next();
            pair_right[i] = s.next();
        }

        //Write your code here
        Set<String> set = new HashSet<>();
        for (int j = 0; j < t; j++) {
            set.add("(" + pair_left[j] + "," + pair_right[j] + ")");
            System.out.println(set.size());
        }
    }

    private static boolean isSolvable(int leap, int[] arr, int i) {
        if (i < 0 || arr[i] == 1) return false;
        if ((i == arr.length - 1) || i + leap > arr.length - 1) return true;

        arr[i] = 1;
        return isSolvable(leap, arr, i + 1) || isSolvable(leap, arr, i - 1) || isSolvable(leap, arr, i + leap);
    }

    private static boolean canWin(int leap, int[] game) {
        return isSolvable(leap, game, 0);
    }

    private static void stackExercise()
    {
        Scanner sc = new Scanner(System.in);
        Stack<Character> stack = new Stack<>();
        while (sc.hasNext()) {
            if (!stack.empty()) stack.clear();
            String input=sc.next();
            //Complete the code
            boolean balanced = true;
            for (char c : input.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') stack.push(c);
                else if (stack.empty()) balanced = false;
                else {
                    char p = stack.peek();
                    if ((c == ')' && p != '(')
                            || (c == '}' && p != '{')
                            || (c == ']' && p != '[')) {
                        balanced = false;
                        break;
                    } else stack.pop();
                }
            }
            balanced = balanced && stack.empty();
            System.out.println(balanced);
        }

    }

    private static void canWinTest() {
        Scanner scan = new Scanner(System.in);
        int q = scan.nextInt();
        while (q-- > 0) {
            int n = scan.nextInt();
            int leap = scan.nextInt();

            int[] game = new int[n];
            for (int i = 0; i < n; i++) {
                game[i] = scan.nextInt();
            }

            System.out.println( (canWin(leap, game)) ? "YES" : "NO" );
        }
        scan.close();
    }
}

class Printer {
    public <T> void printArray(T [] ts) {
        for (T t : ts) {
            System.out.println(t);
        }
    }
}

class Player{
    String name;
    int score;

    Player(String name, int score){
        this.name = name;
        this.score = score;
    }
}

class Checker implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        if (o1.score < o2.score) return 1;
        else if (o1.score > o2.score) return -1;
        else return o1.name.compareTo(o2.name);
    }
}

class Student {
    private int id;
    private String fname;
    private double cgpa;
    public Student(int id, String fname, double cgpa) {
        super();
        this.id = id;
        this.fname = fname;
        this.cgpa = cgpa;
    }
    public int getId() {
        return id;
    }
    public String getFname() {
        return fname;
    }
    public double getCgpa() {
        return cgpa;
    }
    public int getID() {
        return id;
    }
    public String getName() {
        return fname;
    }
    public double getCGPA() {
        return cgpa;
    }
}

class Priorities {
    List<Student> getStudents(List<String> events) {
        PriorityQueue<Student> pq = new PriorityQueue<>(
                Comparator.comparing(Student::getCGPA).reversed().thenComparing(
                        Student::getName).thenComparing(Student::getID));

        for (String event : events) {
            if (event.startsWith("ENTER")) {
                String[] arr = event.split(" ");
                Student student = new Student(Integer.parseInt(arr[3]), arr[1], Double.parseDouble(arr[2]));
                pq.add(student);
                System.out.println("Current queue:");
                pq.forEach(s -> System.out.println(s.getName() + "," + s.getCGPA() + "," + s.getID()));
            } else if (event.startsWith("SERVED")) {
                if (!pq.isEmpty()) {
                    Student s = pq.poll();
                    System.out.println("Served:" + s.getName());
                    System.out.println("Current queue:");
                    pq.forEach(s1 -> System.out.println(s1.getName() + "," + s1.getCGPA() + "," + s1.getID()));
                }
            }
        }
        List<Student> list = new ArrayList<>(pq);
        list.sort(Comparator.comparing(Student::getCGPA).reversed().thenComparing(
                Student::getName).thenComparing(Student::getID));
        return list;
    }
}

class MyCalculator {
    public long power(int n, int p) throws Exception {
        if (n < 0 || p < 0) throw new Exception("n or p should not be negative.");
        if (n == 0 && p == 0) throw new Exception("n and p should not be zero.");
        return (long) Math.pow(n, p);
    }
}

class Add {
    public void add(int... as) {
        int sum = 0;
        boolean first = true;
        for (int a : as) {
            if (first) {
                System.out.print(a);
                first = false;
            } else {
                System.out.print("+" + a);
            }
            sum += a;
        }
        System.out.print("=" + sum + "\n");
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface FamilyBudget {
    String userRole() default "GUEST";
    int budgetLimit() default 0;
}

class FamilyMember {
    @FamilyBudget(userRole = "SENIOR", budgetLimit = 100)
    public void seniorMember(int budget, int moneySpend) {
        System.out.println("Senior Member");
        System.out.println("Spend: " + moneySpend);
        System.out.println("Budget Left: " + (budget - moneySpend));
    }

    @FamilyBudget(userRole = "JUNIOR", budgetLimit = 50)
    public void juniorUser(int budget, int moneySpend) {
        System.out.println("Junior Member");
        System.out.println("Spend: " + moneySpend);
        System.out.println("Budget Left: " + (budget - moneySpend));
    }
}

class MyMath {
    public PerformOperation isOdd() {
        return (int a) -> a % 2 != 0;
    }
    public PerformOperation isPrime() {
        return (int a) -> java.math.BigInteger.valueOf(a).isProbablePrime(2);
    }
    public PerformOperation isPalindrome() {
        return (int a) -> Integer.toString(a).equals(new StringBuilder(Integer.toString(a)).reverse().toString());
    }

    public boolean checker(PerformOperation op, int num) {
        return op.check(num);
    }
}

interface PerformOperation {
    boolean check(int a);
}

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}
