package grid;

import characters.Character;
import characters.Enemy;
import shop.Shop;

import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private static Grid grid = new Grid();
    private int height, width;
    private Cell currentCell;
    private characters.Character character;

    private Grid(){
    }

    public static Grid getInstance(){
        return grid;
    }

    public Grid genMapTest(int width, int height, characters.Character character){
        this.character = character;
        this.width = width;
        this.height = height;

        // initialise cells
        for(int i = 0; i < height; i++){
            add(new ArrayList<>(width));
            for(int j = 0; j < width; j++){
                get(i).add(new Cell(j, i));
                get(i).get(j).setType(Cell.CellType.EMPTY);
                get(i).get(j).setObj(new Empty());
            }
        }
        currentCell = get(0).get(0);
        currentCell.visit();

        get(height-1).get(width-1).setType(Cell.CellType.FINISH);
        get(height-1).get(width-1).setObj(new Finish());

        get(0).get(3).setType(Cell.CellType.SHOP);
        get(0).get(3).setObj(new Shop());

        get(1).get(3).setType(Cell.CellType.SHOP);
        get(1).get(3).setObj(new Shop());

        get(2).get(0).setType(Cell.CellType.SHOP);
        get(2).get(0).setObj(new Shop());

        get(3).get(4).setType(Cell.CellType.ENEMY);
        get(3).get(4).setObj(new Enemy());

        return this;
    }

    public Grid genMap(int width, int height, characters.Character character){
        this.character = character;
        this.width = width;
        this.height = height;
        Random rand = new Random();

        int enemies = rand.nextInt(width*height - 7) + 4;
        int shops = rand.nextInt(width*height - enemies - 3) + 2;

        // initialise cells
        for(int i = 0; i < height; i++){
            add(new ArrayList<>(width));
            for(int j = 0; j < width; j++){
                get(i).add(new Cell(j, i));
                get(i).get(j).setType(Cell.CellType.EMPTY);
                get(i).get(j).setObj(new Empty());
            }
        }
        currentCell = get(0).get(0);
        currentCell.visit();

        get(height-1).get(width-1).setType(Cell.CellType.FINISH);
        get(height-1).get(width-1).setObj(new Finish());

        // generate at least 4 enemies
        for(int i = 0; i < enemies; i++){
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);

            while(get(y).get(x).getType() != Cell.CellType.EMPTY ||
                    (x == width-1 && y == height-1) || (x == 0 && y == 0)){
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            get(y).get(x).setType(Cell.CellType.ENEMY);
            get(y).get(x).setObj(new Enemy());
        }

        // generate at least 2 shops
        for(int i = 0; i < shops; i++){
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);

            while(get(y).get(x).getType() != Cell.CellType.EMPTY ||
                    (x == width-1 && y == height-1) || (x == 0 && y == 0)){
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            }
            get(y).get(x).setType(Cell.CellType.SHOP);
            get(y).get(x).setObj(new Shop());
        }
        return this;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public Character getCharacter() {
        return character;
    }

    // check if coins are available in cell, in case it is empty
    private void coinsAvailableIfEmpty(){
        if(currentCell.getType() == Cell.CellType.EMPTY){
            int random = new Random().nextInt(5);
            if(random == 1){
                random = new Random().nextInt(20);
                System.out.println("Wow! Just found " + random + " coins!");
                character.getInventory().earnCoins(random);
            }
        }
    }

    public boolean goNorth(){
        if(currentCell.getOy() - 1 >= 0) {
            currentCell = get(currentCell.getOy() - 1).get(currentCell.getOx());
            currentCell.visit();
            currentCell.incrementTimesVisited();
            coinsAvailableIfEmpty();
            return true;
        }
        else
            return false;
    }

    public boolean goSouth(){
        if(currentCell.getOy() + 1 < height) {
            currentCell = get(currentCell.getOy() + 1).get(currentCell.getOx());
            currentCell.visit();
            currentCell.incrementTimesVisited();
            coinsAvailableIfEmpty();
            return true;
        }
        else
            return false;
    }

    public boolean goEast(){
        if(currentCell.getOx() + 1 < width) {
            currentCell = get(currentCell.getOy()).get(currentCell.getOx() + 1);
            currentCell.visit();
            currentCell.incrementTimesVisited();
            coinsAvailableIfEmpty();
            return true;
        }
        else
            return false;
    }

    public boolean goWest(){
        if(currentCell.getOx() - 1 >= 0) {
            currentCell = get(currentCell.getOy()).get(currentCell.getOx() - 1);
            currentCell.visit();
            currentCell.incrementTimesVisited();
            coinsAvailableIfEmpty();
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\nGRID:\n");
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(get(i).get(j).isVisited()){
                    if(currentCell.getOx() == j && currentCell.getOy() == i){
                        char character = get(i).get(j).getObj().toCharacter();
                        out.append(java.lang.Character.toLowerCase(character)).append(" ");
                    }
                    else{
                        out.append(get(i).get(j).getObj().toCharacter()).append(" ");
                    }
                }
                else{
                    out.append("? ");
                }
            }
            out.append("\n");
        }
        return out.toString();
    }
    public String showAllGrid(){
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                out.append(get(i).get(j).getObj().toCharacter()).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}
