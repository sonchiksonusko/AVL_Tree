import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

//import sun.reflect.generics.tree.Tree;

public class Movie implements Comparable<Movie> {

    String Poster_link;
    String Series_Title;
    String Released_Year;
    String Certificate;
    String Runtime;
    String Genre;
    Double IMDB_Rating;
    String Overview;
    String Meta_score;
    String Director;
    ArrayList<String> Star;
    String No_of_votes;
    String Gross;
    

    // Constructor
    public Movie(String poster_link, String series_Title, String released_Year, String certificate, String runtime,
                 String genre, Double imdb_Rating, String overview, String meta_score, String director,
                 ArrayList<String> star, String no_of_votes, String gross) {
        Poster_link = poster_link;
        Series_Title = series_Title;
        Released_Year = released_Year;
        Certificate = certificate;
        Runtime = runtime;
        Genre = genre;
        IMDB_Rating = imdb_Rating;
        Overview = overview;
        Meta_score = meta_score;
        Director = director;
        Star = star;
        No_of_votes = no_of_votes;
        Gross = gross;

    }

    @Override
    public int compareTo(Movie other) {
        return this.IMDB_Rating.compareTo(other.IMDB_Rating);
    }
    public static void main(String[] args) {
        // Ensure the correct file path is provided
        //"C:/Users/Соня/Downloads/imdb_test.csv"
        //"C:/Users/Соня/Desktop/imdb_top_1000.csv"
        ArrayList<Movie> movies = readMoviesFromCSV("C:/Users/Соня/Desktop/imdb_top_1000.csv");
        AVLTree<Movie> ratingTree = new AVLTree<>(new Comparators.IMDB_Rating_Comparator());

        //System.out.print(movies);
        for (Movie m : movies) {
        
            ratingTree.insert(m);
         
        }
        System.out.print(" BY RATING");
        System.out.println("root: "+ratingTree.root.data);
        System.out.println("max: "+ratingTree.findMax(ratingTree.root).data);
        System.out.println("min: "+ratingTree.findMin(ratingTree.root).data);

        ratingTree.inOrderTraversal(ratingTree.root);

          AVLTree<Movie> yearTree = new AVLTree<>(new Comparators.Released_YearComparator());
        for (Movie m : movies) {
            yearTree.insert(m);
        }
        System.out.print("BY YEAR");
        System.out.println("root: "+yearTree.root.data);
        System.out.println("max: "+ yearTree.findMax(yearTree.root).data);
        System.out.println("min: "+yearTree.findMin(yearTree.root).data);
        yearTree.inOrderTraversal(yearTree.root);
        underatedMovies(yearTree.root,0);



         AVLTree<Movie> titleTree = new AVLTree<>(new Comparators.Series_Title_Comparator());
        for (Movie m : movies) {
            titleTree.insert(m);
        }
        System.out.print("BY TITLE");
        System.out.println("root: "+titleTree.root.data);
        System.out.println("max: "+titleTree.findMax(titleTree.root).data);
        System.out.println("min: "+titleTree.findMin(titleTree.root).data);
        titleTree.inOrderTraversal(titleTree.root);


        AVLTree<Movie> grossTree = new AVLTree<>(new Comparators.Gross_Collection_Comparator());
        for (Movie m : movies) {
            grossTree.insert(m);
        }
        System.out.print("BY GROSS");
        System.out.println("root: "+grossTree.root.data);
        System.out.println("max: "+grossTree.findMax(grossTree.root).data);
        System.out.println("min: "+grossTree.findMin(grossTree.root).data);
        grossTree.inOrderTraversal(grossTree.root);


      

       // int count = 0;
       // System.out.print("----------------------------------------------------------------" + root.data+"  ");
        //underatedMovies(root,count);






    }

    public static ArrayList<Movie> readMoviesFromCSV(String myFile){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            String line;
            boolean firstLine = true;  // Skip the header line
            while ((line = br.readLine()) != null) {
                if (firstLine) { // Skip the first header line
                    firstLine = false;
                    continue;
                }

                // Split by commas, but handle cases where commas are inside quotes (e.g., movie descriptions)
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String poster_link = values[0];
                String series_Title = values[1];
                String released_Year = values[2];
                String certificate = values[3];
                String runtime = values[4];
                String genre = values[5];
                Double imdb_Rating = values[6].isEmpty() ? 0.0 : Double.parseDouble(values[6]);
                String overview = values[7];
                String meta_score = values[8];
                String director = values[9];

                // Split stars by '|' (pipe) character
                ArrayList<String> star = new ArrayList<String>(Arrays.asList(values[10].split("\\|")));
                
                String no_of_votes = values[13];
                String gross = values[14];

                Movie movie = new Movie(poster_link, series_Title, released_Year, certificate, runtime, genre,
                        imdb_Rating, overview, meta_score, director, star, no_of_votes, gross);
                movies.add(movie);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }


public static int underatedMovies(TreeNode<Movie> root, int count){
    if(root == null){
        return count;
    }
    TreeNode<Movie> current = root;
    long leftgross=Long.MAX_VALUE;
    long rightgross=Long.MAX_VALUE;
    long currentGross = parseGross(root.data.getGross());

    if(current.leftChild!=null){// Check if left child exists
        leftgross = parseGross(root.leftChild.data.getGross()); // Parse the gross value of the left child
    }
    if(current.rightChild!=null){
        rightgross = parseGross(root.rightChild.data.getGross()); 
    }
    if(currentGross< leftgross && currentGross<rightgross ){// Check if current node's gross is less than both children
        System.out.println("Underated movie:   "+current.data);
        count++;
    }
    count = underatedMovies(root.leftChild, count);
    count = underatedMovies(root.rightChild, count);
    System.out.println("COUNT:"+count);
    return count;
}





public boolean isUnderrated(TreeNode<Movie> root, String title){
    TreeNode<Movie> movie = findMovieByTitle(root, title);
    if(movie == null){
        return false; // Movie not found
    }
    if(movie.leftChild == null || movie.rightChild == null){
        return false; // Movie is not underrated
    }
    if(parseGross(movie.data.getGross())< parseGross(movie.leftChild.data.getGross()) && parseGross(movie.data.getGross())<parseGross(movie.rightChild.data.getGross())){
        return true; // Movie is underrated
    }
    if(movie.leftChild.data.getGross() == null || movie.rightChild.data.getGross() == null){
        return false;
    }
    long leftGross = parseGross(movie.leftChild.data.getGross());
    long rightGross = parseGross(movie.rightChild.data.getGross()); 
    long currentGross = parseGross(movie.data.getGross());

    return currentGross < leftGross && currentGross < rightGross; // Check if the movie is underrated
}




public TreeNode<Movie> findMovieByTitle(TreeNode<Movie> root, String title) {
    if (root == null) {
        return null;
    }
    Stack<TreeNode<Movie>> s = new Stack<>();// Stack to hold Movies
    s.push(root);
    while (!s.isEmpty()) {
        TreeNode<Movie> current = s.pop();// Get the current node
        if (current.data.getSeries_Title().equalsIgnoreCase(title.toString())) { // Check if the current node's title matches the criteria
            return current;
        }
        if (current.rightChild != null) {// Check if right child exists
            s.push(current.rightChild);
        }
        if (current.leftChild != null) {
            s.push(current.leftChild);
        }
    }
    return null; // Movie not found
    
}


public Double averageIMDB(TreeNode<Movie> root, String star){
    if(root == null){
        return 0.0;
    }
    Stack<TreeNode<Movie>> s = new Stack<>(); // Stack to hold Movies   
    s.push(root);
    double sum = 0.0;
    int count = 0;
    while(!s.isEmpty()){
        TreeNode<Movie> current = s.pop();
        if(current.data.getStar().contains(star)){ // Check if the current node's star matches the criteria
            sum += current.data.getIMDB_Rating(); // Add the IMDB rating to the sum
            count++; // Increment the count
        }
        if(current.rightChild != null){ // Check if right child exists
            s.push(current.rightChild);
        }
        if(current.leftChild != null){ // Check if left child exists
            s.push(current.leftChild);
        }
    }
    return count==0? 0.0 :sum/count; // Return the average IMDB rating
}






private static long parseGross(String grossStr) {
    if (grossStr == null || grossStr.isEmpty()) {
        return 0;
    }
    try {
        return Long.parseLong(grossStr.replace(",", "").replace("\"", ""));
    } catch (NumberFormatException e) {
        return 0;
    }
}






public double profit(TreeNode<Movie> root, String star){
    if(root == null || star == null){
        return 0.0;
    }
    Stack<TreeNode<Movie>> s = new Stack<>(); // Stack to hold Movies
    long profit =0;
    s.push(root);
    while(!s.isEmpty()){

        TreeNode<Movie> current = s.pop(); // Get the current node
        if(current.rightChild != null){ // Check if right child exists
                s.push(current.rightChild);
            }
        if(current.leftChild != null){ // Check if left child exists
                s.push(current.leftChild);
            }

        if(current.data.getStar().contains(star)){
            profit = parseGross(current.data.getGross())+profit;
          
       }
    }
    System.out.println("Profit of movies with star " + star + ": " + profit);
    return profit;
}

public void star_movie_data(TreeNode<Movie> root , String star){////////////////////////////////////////////////////////////////////////////
    if(root == null || star == null){
        return;
    }
    double runtime = 0.0;
    int firstfilm = 3000;
    int lastfilm = 0;
    Stack<TreeNode<Movie>> s = new Stack<>(); // Stack to hold Movies
    s.push(root);
    while(!s.isEmpty()){
        TreeNode<Movie> current = s.pop(); // Get the current node
        if(current.rightChild != null){ // Check if right child exists
                s.push(current.rightChild);
            }
        if(current.leftChild != null){ // Check if left child exists
                s.push(current.leftChild);
            }

        if(current.data.getStar().contains(star)){
            runtime = Double.parseDouble(current.data.getRuntime())+runtime;
       
             if(Integer.parseInt(current.data.getReleased_Year())<firstfilm){
                 firstfilm = Integer.parseInt(current.data.getReleased_Year());
              }
             if(Integer.parseInt(current.data.getReleased_Year())>lastfilm){
                  lastfilm = Integer.parseInt(current.data.getReleased_Year());
                } 
         }
       }
       System.out.println("Star: " + star);
       System.out.println("First film year: " + firstfilm); 
       System.out.println("Last film year: " + lastfilm);
       System.out.println("Total runtime: " + runtime + " minutes");
    }





    // Getters and Setters
    public String getPoster_link() {
        return Poster_link;
    }

    public void setPoster_link(String poster_link) {
        Poster_link = poster_link;
    }

    public String getSeries_Title() {
        return Series_Title;
    }

    public void setSeries_Title(String series_Title) {
        Series_Title = series_Title;
    }

    public String getReleased_Year() {
        return Released_Year;
    }

    public void setReleased_Year(String released_Year) {
        Released_Year = released_Year;
    }

    public String getCertificate() {
        return Certificate;
    }

    public void setCertificate(String certificate) {
        Certificate = certificate;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Double getIMDB_Rating() {
        return IMDB_Rating;
    }

  public void setIMDB_Rating(Double imdb_Rating) {
        IMDB_Rating = imdb_Rating;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getMeta_score() {
        return Meta_score;
    }

    public void setMeta_score(String meta_score) {
        Meta_score = meta_score;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public ArrayList<String> getStar() {
        return Star;
    }

    public void setStar(ArrayList<String> star) {
        Star = star;
    }

    public String getNo_of_votes() {
        return No_of_votes;
    }

    public void setNo_of_votes(String no_of_votes) {
        No_of_votes = no_of_votes;
    }

    public String getGross() {
        return Gross;
    }

    public void setGross(String gross) {
        Gross = gross;
    }
public String toString() {
        return "Movie :" +
               " Series_Title='" + Series_Title + '\'' +
                ", Released_Year='" + Released_Year + '\'' +
               // ", Runtime='" + Runtime + '\'' +
                 ", Gross='" + Gross + '\'' +
                ", IMDB_Rating=" + IMDB_Rating +
                //", Meta_score='" + Meta_score + '\'' +
               // ", Director='" + Director + '\'' +

                '}';
    }
  
}

