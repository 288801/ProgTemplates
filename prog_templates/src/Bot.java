import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class Bot {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        solve(in, out);
        out.close();
    }

    static class Tank {
        double x = -1;
        double y = -1;
        double r = -1;
        double angle = -1;

        double health = -1;
        double max_health = -1;
        double speed = -1;

        double damage = -1;
        double bullet_speed = -1;

        public Tank() {}

        public Tank(double x, double y, double r, double angle, double health, double max_health, double speed, double damage, double bullet_speed) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.angle = angle;
            this.health = health;
            this.max_health = max_health;
            this.speed = speed;
            this.damage = damage;
            this.bullet_speed = bullet_speed;
        }
    }

    static class Enemy {
        int score = -1;
        int uid = -1;
        Tank tank = new Tank();
        List<Bullet> bullets = new LinkedList<>();

        public Enemy() {}

        public Enemy(int score, int uid, Tank tank, List<Bullet> bullets) {
            this.score = score;
            this.uid = uid;
            this.tank = tank;
            this.bullets = bullets;
        }
    }

    static class Bullet {
        double x = -1;
        double y = -1;
        double r = -1;

        public Bullet() {}

        public Bullet(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
    }

    static class BonusMark {
        double x = -1;
        double y = -1;
        double r = -1;
        double h = -1;

        public BonusMark() {
        }

        public BonusMark(double x, double y, double r, double h) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.h = h;
        }
    }

    /* create jar file:
    javac Java_bot.java
    jar cmvf MANIFEST.MF myJar.jar *.class
    # запустить
    java -jar myJar.jar
    * */
    static void solve(InputReader in, PrintWriter out) {

        String s;
        s = in.next();
        if (s.equals("Defeat")) {
            return;
        }
        int tick = Integer.parseInt(s);
        double my_score = in.nextDouble();

        Tank my_tank = new Tank(in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble());

        int my_bullets_count = in.nextInt();
        List<Bullet> my_bullets = new ArrayList<>(my_bullets_count);
        for (int i = 0; i < my_bullets_count; i++) {
            Bullet bullet = new Bullet(in.nextDouble(), in.nextDouble(), in.nextDouble());
            my_bullets.add(bullet);
        }

        int enemies_count = in.nextInt();

        Enemy[] enemies = new Enemy[enemies_count];

        for (int i = 0; i < enemies_count; i++) {
            enemies[i] = new Enemy();
            int is_enemy_alive = in.nextInt();
            enemies[i].score = in.nextInt();
            enemies[i].uid = in.nextInt();

            if (is_enemy_alive == 1) {
                enemies[i].tank.health = in.nextDouble();
                enemies[i].tank.x = in.nextDouble();
                enemies[i].tank.y = in.nextDouble();
                enemies[i].tank.r = in.nextDouble();
                enemies[i].tank.angle = in.nextDouble();
            }

            int enemy_bullets_count = in.nextInt();

            for (int j = 0; j < enemy_bullets_count; j++) {
                enemies[i].bullets.add(new Bullet(in.nextDouble(), in.nextDouble(), in.nextDouble()));
            }
        }

        int bonus_mark_count = in.nextInt();

        BonusMark[] bonus_marks = new BonusMark[bonus_mark_count];

        for (int i = 0; i < enemies_count; i++) {
            bonus_marks[i] = new BonusMark(in.nextDouble(), in.nextDouble(), in.nextDouble(), in.nextDouble());
        }

        String memory_string = in.nextLine();

//        if (tick < 7000) {
//            BonusMark best = bonus_marks[0];
//            double minD = Double.MAX_VALUE;
//            for (BonusMark mark : bonus_marks) {
//                double dx = mark.x - my_tank.x;
//                double dy = mark.y - my_tank.y;
//                if (Math.sqrt((dx * dx) + (dy * dy)) < minD) {
//                    minD = Math.sqrt((dx * dx) + (dy * dy));
//                    best = mark;
//                }
//            }
//
//
//        }

//        out.println("move " + (my_tank.x + ThreadLocalRandom.current().nextDouble() * 10) + " " + (my_tank.y + ThreadLocalRandom.current().nextDouble() * 10));
        out.println("turn " + 0.1);

        Dot dot = isBulletNear(enemies, my_tank);
        if(dot == null){

        } else{
            out.println("move " + dot.x + " " + dot.y);
        }
        out.println("shoot");
    }

    public static class Dot{
        double x;
        double y;

        public Dot(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static Dot isBulletNear(Enemy[] enemies, Tank myTank){
        for(Enemy enemy : enemies){
            for(Bullet bullet : enemy.bullets){
                if(isBulletDangerous(myTank.x, myTank.y, enemy.tank.x, enemy.tank.y, bullet.x,bullet.y, myTank.r, bullet.r)){
                    double k = -(enemy.tank.y-myTank.y)/(enemy.tank.x-myTank.x);
                    double b = myTank.y - k*myTank.x;
                    double x = Math.sin(Math.atan(k))*(2*myTank.r + 2*bullet.r);
                    double y = k*x + b;
                    return new Dot(x ,y);
                }
            }
        }
        return null;
    }

    public static boolean isBulletDangerous(double x1, double y1, double x2, double y2, double xB, double yB, double R, double r){
        double k = (y2-y1)/(x2-x1);
        double b = -(y2-y1)*x1/(x2-x1)- y1; // y = k*x + b
        double b12 = (R+r*2+2)/Math.cos(Math.atan(k));
        if(yB <= k*xB + (b+b12) && yB >= k*xB + (b-b12) && Math.abs(x1 - xB) <= 10*R){
            return true;
        }else{
            return false;
        }
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public String nextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }
}