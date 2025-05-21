import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Movie implements Comparable<Movie> {

    String Poster_link;
    String Series_Title;
    Integer Released_Year;
    String Certificate;
    Integer Runtime;
    String Genre;
    Double IMDB_Rating;
    String Overview;
    String Meta_score;
    String Director;
    List<String> Star;
    String No_of_votes;
    String Gross;
    

    // Constructor
    public Movie(String poster_link, String series_Title, Integer year, String certificate, Integer runtimeMinutes,
                 String genre, Double imdb_Rating, String overview, String meta_score, String director,
                 List<String> star, String no_of_votes, String gross) {
        Poster_link = poster_link;
        Series_Title = series_Title;
        Released_Year = year;
        Certificate = certificate;
        Runtime = runtimeMinutes;
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
      

        ArrayList<Movie> movies = readMoviesFromCSV("C:/Users/Соня/Desktop/imdb_top_1000.csv");
        AVLTree<Movie> ratingTree = new AVLTree<>(new Comparators.IMDB_Rating_Comparator());// Comparator for IMDB rating
        for (Movie m : movies) {
        
            ratingTree.insert(m);
         
        }
        System.out.print("BY RATING "+"\n");
        System.out.println("    root: "+ratingTree.root.data);
        System.out.println("    max: "+ratingTree.findMax(ratingTree.root).data);
        System.out.println("    min: "+ratingTree.findMin(ratingTree.root).data);

     

        AVLTree<Movie> yearTree = new AVLTree<>(new Comparators.Released_YearComparator());// Comparator for released year
        for (Movie m : movies) {
            if (m.getReleased_Year() != -1 && !m.getReleased_Year().toString().isEmpty()) {// Check if the released year is not -1 and not empty
            yearTree.insert(m);
        }
       }
        System.out.print("BY YEAR  "+"\n");
        System.out.println("  root: "+yearTree.root.data);
        System.out.println("  max: "+ yearTree.findMax(yearTree.root).data);
        System.out.println("  min: "+yearTree.findMin(yearTree.root).data);

      
       


        AVLTree<Movie> titleTree = new AVLTree<>(new Comparators.Series_Title_Comparator());
        for (Movie m : movies) {
            if (m.getSeries_Title() != null && !m.getSeries_Title().isEmpty()) {
                titleTree.insert(m);
        }
            }
        System.out.print("BY TITLE"+"\n");
        System.out.println("   root: "+titleTree.root.data);
        System.out.println("   max: "+titleTree.findMax(titleTree.root).data);
        System.out.println("   min: "+titleTree.findMin(titleTree.root).data);



        AVLTree<Movie> grossTree = new AVLTree<>(new Comparators.Gross_Collection_Comparator());
        for (Movie m : movies) {
            if (m.getGross() != null && !m.getGross().isEmpty()) {
                grossTree.insert(m);
        }
        }
        System.out.print("BY GROSS   "+"\n");
        System.out.println("   root: "+grossTree.root.data);
        System.out.println("   max: "+grossTree.findMax(grossTree.root).data);
        System.out.println("   min: "+grossTree.findMin(grossTree.root).data);
       

       System.out.print("Print movies after year "+"\n");
       printMoviesAfter(titleTree.root, yearTree.root, "Joker");
    
       
        System.out.print("------------------------------------------------------"+"\n");

        System.out.print(underatedMovies(ratingTree.root)+" \n");
        System.out.print("Check is Underrated   "+" \n");
        System.out.print("Joker is Underrated? " + isUnderrated(ratingTree.root, "Joker") + "\n");
        System.out.print("Paths of Glory is Underrated? " + isUnderrated(ratingTree.root, "Paths of Glory") + "\n");
        System.out.print("------------------------------------------------------"+"\n");

        averageIMDB(ratingTree.root, "Robert Downey Jr.");
        profit(ratingTree.root, "Robert Downey Jr.");
        star_movie_data(ratingTree.root, "Robert Downey Jr.");
        System.out.print("------------------------------------------------------"+"\n");
        
        averageIMDB(grossTree.root, "Christian Bale");
        profit(grossTree.root, "Christian Bale");
        star_movie_data(grossTree.root, "Christian Bale");





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
                // Split by commas, but handle cases where commas are inside quotes 
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String poster_link = values[0].isEmpty() ? "-1" : values[0];
                String series_Title = values[1].isEmpty() ? null : values[1];
                String released_Year = values[2].replaceAll("\\D", "");  // keep only digits
                int year =released_Year.isEmpty() ? -1 : Integer.parseInt(released_Year);        
                String certificate = values[3].isEmpty() ? "-1" : values[3];
                String runtimeDigits = values[4].replaceAll("\\D", ""); // "128"  (\\D = any non-digit)
                int runtimeMinutes  = runtimeDigits.isEmpty() ? 0: Integer.parseInt(runtimeDigits);
                String genre = values[5].isEmpty() ? "-1" : values[5];
                Double imdb_Rating = values[6].isEmpty() ? null : Double.parseDouble(values[6]);
                String overview = values[7].isEmpty() ? "-1" : values[7];
                String meta_score = values[8].isEmpty() ? "-1" : values[8];
                String director = values[9].isEmpty() ? "-1" : values[9];
                // Split stars by '|' (pipe) character
                // gather up to four star columns (10-13)
                List<String> star = new ArrayList<>();
                for (int i = 10; i <= 13 && i < values.length; i++) {
                    String s = values[i].trim();
                    if (!s.isEmpty()) {
                        star.add(s);
                    }
                }

                
                String no_of_votes = (values.length > 14) ? values[14].trim() : "0";
                String gross= (values.length > 15) ? values[15].replaceAll("\\D", "") : null;

                Movie movie = new Movie(poster_link, series_Title, year, certificate, runtimeMinutes, genre,
                        imdb_Rating, overview, meta_score, director, star, no_of_votes, gross);
                movies.add(movie);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

public static void printMoviesAfter(TreeNode<Movie> titleTree,TreeNode<Movie> yearTree, String title){
    TreeNode<Movie> movie = findMovieByTitle(titleTree, title);
      if (movie == null) {
        System.out.println("Movie \"" + title + "\" not found.");
        return;
    }
    int targetyear = movie.data.getReleased_Year();
    System.out.println("Movies released after " + targetyear + ":");
    printAfter(yearTree, targetyear);
 
}

public static void printAfter(TreeNode<Movie> root,int year){
    if(root == null){
        return;
    }
    if(root.data.getReleased_Year()>year){
        
        printAfter(root.leftChild, year);
        System.out.println(root.data.getSeries_Title() + ", " + root.data.getReleased_Year());
        printAfter(root.rightChild, year);
    }
    else {
        printAfter(root.rightChild, year);
    }
}


public static int underatedMovies(TreeNode<Movie> root){
    if(root == null){
        return 0;
    }
    int count = 0;
    if (root.leftChild != null && root.rightChild != null) {// Check if both children exist
        Double rating = root.data.getIMDB_Rating();// Check if the current node has an IMDB rating
        if(rating != null ) {

        long currentGross = parseGross(root.data.getGross());// Check if the current node has a gross value
        long leftgross = parseGross(root.leftChild.data.getGross());
        long rightgross = parseGross(root.rightChild.data.getGross());
   
        if(currentGross< leftgross && currentGross<rightgross ){// if current node's gross is less than both children
            System.out.println("Underated movie:   "+ root.data.getSeries_Title());
            count=1;
        }
    }
    }
     count +=underatedMovies(root.leftChild);// Check left child
     count +=underatedMovies(root.rightChild);
    return count;// Count the number of underrated movies
}





public static boolean isUnderrated(TreeNode<Movie> root, String title){
    TreeNode<Movie> movie = findMovieByTitle(root, title);

    if(movie == null){
        return false; // Movie not found
    }
    if(movie.leftChild == null || movie.rightChild == null){
        return false; // Movie is not underrated
    }
    if(movie.data.getIMDB_Rating()==null){
        return false; // Movie has no IMDB rating
    }
   
    if(movie.leftChild.data.getGross() == null || movie.rightChild.data.getGross() == null||movie.data.getGross()==null){// Check if the current node has a gross value
        return false;
    }
    long leftGross = parseGross(movie.leftChild.data.getGross());
    long rightGross = parseGross(movie.rightChild.data.getGross()); 
    long currentGross = parseGross(movie.data.getGross());

    return currentGross < leftGross && currentGross < rightGross; // Check if the movie is underrated
}




public static TreeNode<Movie> findMovieByTitle(TreeNode<Movie> root, String title) {
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


public static Double averageIMDB(TreeNode<Movie> root, String star){
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
    System.out.print("Average IMDB rating of movies with star  " + star + ": "+ sum/count + "\n"); 
    return count==0? 0.0 :sum/count; // Return the average IMDB rating
}






public static long parseGross(String gross) {
    if (gross == null || gross.equals("-1") || gross.isEmpty()) {
        return 0;
    }
    try {
        return Long.parseLong(gross);
    } catch (NumberFormatException e) {
        return 0;
    }
}





public static long profit(TreeNode<Movie> root, String star){
    if(root == null || star == null){
        return 0;
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

public static void star_movie_data(TreeNode<Movie> root , String star){
    if(root == null || star == null){
        return;
    }
    int runtime = 0;
    int firstfilm = Integer.MAX_VALUE;
    int lastfilm = Integer.MIN_VALUE;
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
            
            runtime = current.data.getRuntime()+runtime;
       
             if(current.data.getReleased_Year()<firstfilm){
                 firstfilm = current.data.getReleased_Year();

              }
             if(current.data.getReleased_Year()>lastfilm){
                  lastfilm = current.data.getReleased_Year();

                } 
         }
       }
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

    public Integer getReleased_Year() {
        return Released_Year;
    }

    public void setReleased_Year(Integer year) {
        Released_Year = year;
    }

    public String getCertificate() {
        return Certificate;
    }

    public void setCertificate(String certificate) {
        Certificate = certificate;
    }

    public Integer getRuntime() {
        return Runtime;
    }

    public void setRuntime(Integer runtime) {
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

    public List<String> getStar() {
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

