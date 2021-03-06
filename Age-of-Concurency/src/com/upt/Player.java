package com.upt;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Thread implements ITaskFinishedCallback {

    private boolean isAlive;

    private int[] resources = new int[4];

    private Hero hero;

    private City city;

    private String playerColor;

    private int trainMercenaryFoodCost = 50;
    private int trainMercenaryGoldCost = 25;

    private LinkedList<Command> commandsQueue = new LinkedList<Command>();

    private static AtomicInteger players;

    static {
        players = new AtomicInteger();
    }

    public Player(int heroPosX, int heroPosY, String playerColor)
    {
        hero = new Hero(heroPosX, heroPosY, this);
        Grid.getInstance().tileFromPosition(heroPosX, heroPosY).setTileContent(hero);
        isAlive = true;

        this.playerColor = playerColor;

        for (int i = 0; i < 4; i++)
            resources[i] = 100;
    }

    public static int getNoPlayers() {
        return players.intValue();
    }

    public synchronized void run()
    {
        players.getAndIncrement();

        while(isAlive) {
            while (!commandsQueue.isEmpty() && isAlive) {
                Command command = commandsQueue.poll();
                System.out.println(command.commandStr);

                switch (command.commandStr) {
                    case "collect":
                        System.out.println(playerColor + "Started collect!\n" + Grid.ANSI_RESET);
                        hero.startCollectingResource(command.resourceType, command.amount, this);
                        try {
                            // System.out.println("Waiting\n");
                            wait();
                        } catch (Exception e) {
                        }
                        break;

                    case "move":
                        System.out.println(playerColor + "Started move to " + command.x + ", " + command.y + "!\n" + Grid.ANSI_RESET);
                        hero.moveToDestination(command.x, command.y, this);
                        // System.out.println(playerColor);
                        // hero.printMap();
                        // System.out.println(Grid.ANSI_RESET);
                        try {
                            // System.out.println("Waiting\n");
                            wait();
                        } catch (Exception e) { }
                        break;

                    case "sendMercenary":

                        Mercenary merc = trainMercenary();
                        System.out.println(playerColor + "m" + merc.getNo() + " HP: " + merc.getHealth() + Grid.ANSI_RESET);
                        if (merc != null) {
                            merc.moveToDestination(command.x, command.y, new ITaskFinishedCallback() {
                                @Override
                                public void onFinish() {
                                    merc.startAttackingAdjacentEnemy(new ITaskFinishedCallback() {
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
                                    // merc died before city was destroyed.
                                }
                            });
                        } else {
                            System.out.println("No mercenary created");
                        }
                        break;

                    case "repairCity":
                        System.out.println(playerColor + "started repairing his city!" + Grid.ANSI_RESET);

                        city.startRepair(new ITaskFinishedCallback() {
                            @Override
                            public void onFinish() {
                                System.out.println("Finished repairing city");
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                        break;

                    default:
                        System.out.println("Unrecognized command will be ignored.");
                        continue;
                }
            }

        }
    }

    public void addCommand(Command command)
    {
        commandsQueue.add(command);
    }

    public synchronized void addResource(Resource.ResourceType resourceType, int value)
    {
        resources[resourceType.ordinal()] += value;
        // System.out.print(this.getName() + " " + resourceType + " value: " + resources[resourceType.ordinal()] + "\n");
    }

    public void die() {
        isAlive = false;
        players.getAndDecrement();
    }

    public Mercenary trainMercenary()
    {
        var tiles = Grid.getInstance().getNeighbours(Grid.getInstance().tileFromPosition(city.getPosX(), city.getPosY()));

        ArrayList<Tile> emptyTiles = new ArrayList<>();
        for (Tile t : tiles)
        {
            if (t.getTileContent() == null)
                emptyTiles.add(t);
        }

        if (emptyTiles.size() > 0)
        {
            System.out.println("EmptyTiles:" + emptyTiles.size());
            if (getResource(Resource.ResourceType.food) >= trainMercenaryFoodCost && getResource(Resource.ResourceType.gold) >= trainMercenaryGoldCost)
            {
                addResource(Resource.ResourceType.food, -trainMercenaryFoodCost);
                addResource(Resource.ResourceType.gold, -trainMercenaryGoldCost);

                System.out.println(playerColor + "Mercenary spawned. Food: " + getResource(Resource.ResourceType.food) + " Gold: " + getResource(Resource.ResourceType.gold) + Grid.ANSI_RESET);

                Mercenary newMercenary = new Mercenary(emptyTiles.get(0).getPosX(), emptyTiles.get(0).getPosY(), this);
                emptyTiles.get(0).setTileContent(newMercenary);

                return newMercenary;
            }
            else
            {
                System.out.println("Not enough resources to train merc!" );
                return  null;
            }
        }
        else
        {
            return null;
        }
    }

    public Hero getHero() {
        return hero;
    }

    public void setCity(City city) { this.city = city; }

    public City getCity() {
        return city;
    }

    public int getResource(Resource.ResourceType resourceType)
    {
        return resources[resourceType.ordinal()];
    }

    public String getPlayerColor() { return playerColor; }

    @Override
    public synchronized void onFinish() {
        System.out.println("onFinish");
        this.notify();
    }

    @Override
    public synchronized void onFail() {
        System.out.println("onFail");
        this.notify();
    }
}
