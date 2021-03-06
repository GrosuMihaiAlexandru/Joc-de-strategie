package com.upt.debugging;

import com.upt.*;

public class AttackerDebug {


    public static void main(String[] args) {
        Grid.setGridPath("scenarios\\resourceCollect01.txt");
        Grid.getInstance().displayGrid();
        System.out.println();

        Player player = new Player(3, 3, Grid.ANSI_BLUE);
        Grid.getInstance().tileFromPosition(3 , 3).setTileContent(player.getHero());

        Player player2 = new Player(4, 3, Grid.ANSI_RED);
        Grid.getInstance().tileFromPosition(4 , 3).setTileContent(player2.getHero());

        City city = new City(2, 2, player);
        player.setCity(city);
        Grid.getInstance().tileFromPosition(2,2).setTileContent(city);

        Mercenary m1 = new Mercenary(1, 2, player2);
        Grid.getInstance().tileFromPosition(1,2).setTileContent(m1);
        Mercenary m2 = new Mercenary(3, 2, player2);
        Grid.getInstance().tileFromPosition(3,2).setTileContent(m2);
        Mercenary m3 = new Mercenary(2, 1, player2);
        Grid.getInstance().tileFromPosition(2,1).setTileContent(m3);

        Grid.getInstance().displayGrid();

        player.trainMercenary();

        Grid.getInstance().displayGrid();

        System.out.println(player.trainMercenary());

        /*m1.startAttackingAdjacentEnemy(new ITaskFinishedCallback() {
            @Override
            public void onFinish()
            {
                city.startRepair(new ITaskFinishedCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFail() {

                    }
                });
            }

            @Override
            public void onFail() {

            }
        });

        m2.startAttackingAdjacentEnemy(new ITaskFinishedCallback() {
            @Override
            public void onFinish()
            {
                System.out.println("m2 finished attacking");
            }

            @Override
            public void onFail() {

            }
        });

        m3.startAttackingAdjacentEnemy(new ITaskFinishedCallback() {
            @Override
            public void onFinish()
            {
                System.out.println("m3 finished attacking");
                Grid.getInstance().displayGrid();
            }

            @Override
            public void onFail() {

            }
        });*/

        /*new Thread(() -> {

            int counter = 0;
            var attackables = m1.getAdjacentAttackers();
            while (m1.isAlive())
            {
                if (attackables.size() > 0)
                {
                    System.out.println("m1 HP: " + m1.getHealth() + " attack no: " + counter);
                    counter++;
                    m1.attack(attackables.get(0));
                }


                try {
                    Thread.sleep((1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int counter = 0;
            var attackables = m2.getAdjacentAttackers();
            while (m2.isAlive())
            {
                if (attackables.size() > 0)
                {
                    System.out.println("m2 HP: " + m2.getHealth() + " attack no: " + counter);
                    counter++;
                    m2.attack(attackables.get(0));
                }

                try {
                    Thread.sleep((1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            int counter = 0;
            var attackables = m3.getAdjacentAttackers();
            while (m3.isAlive())
            {
                if (attackables.size() > 0)
                {
                    System.out.println("m3 HP: " + m3.getHealth() + " attack no: " + counter);
                    counter++;
                    m3.attack(attackables.get(0));
                }

                try {
                    Thread.sleep((1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        //player.start();
        //player2.start();
    }
}
