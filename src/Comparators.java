
import java.util.Comparator;
import java.util.List;
public class Comparators {// Comparator class to compare Movie objects based on different attributes
    
    public static  class Poster_link_Comparator implements Comparator<Movie> { // Comparator for poster link
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getPoster_link().compareTo(m2.getPoster_link());
        }        
    }

    public  static class Series_Title_Comparator implements Comparator<Movie> { // Comparator for series title
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getSeries_Title().compareTo(m2.getSeries_Title());
        }
    }

    public static class Released_YearComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie m1, Movie m2) {
        try {
            return Integer.compare(
                m1.getReleased_Year(),
                m2.getReleased_Year()
            );
        } catch (NumberFormatException e) {
            return 0; // Or handle missing values as lower/higher priority
        }
    }
    }
    public static  class Certificate_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getCertificate().compareTo(m2.getCertificate());
        }
    }
    public static class Runtime_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getRuntime().compareTo(m2.getRuntime());
        }
    }
    public static  class Genre_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getGenre().compareTo(m2.getGenre());
        }
    }
    public static class IMDB_Rating_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return Double.compare(m1.getIMDB_Rating(), m2.getIMDB_Rating());
        }
    }

    public static class Overview_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getOverview().compareTo(m2.getOverview());
        }   
    }   

    public static  class Meta_score_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return Integer.compare(Integer.parseInt(m1.getMeta_score()), Integer.parseInt(m2.getMeta_score()));
        }
    }
    public static  class Director_Comparator implements Comparator<Movie>{
        public int compare(Movie m1, Movie m2) {
            return m1.getDirector().compareTo(m2.getDirector());
        }
    }
    public static  class Stars_Comparator implements Comparator<Movie>{
       @Override
        public int compare(Movie m1, Movie m2) {
        List<String> actors1 = m1.getStar(); // Assuming getStars() returns List<String>
        List<String> actors2 = m2.getStar();

        if (actors1.size() == 0 && actors2.size() == 0) return 0;
        if (actors1.size() == 0) return -1;
        if (actors2.size() == 0) return 1;

        return actors1.get(0).compareTo(actors2.get(0)); // Compare by first actor
    }
    }
    public static  class No_of_Votes_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return Integer.compare(Integer.parseInt(m1.getNo_of_votes()),Integer.parseInt(m2.getNo_of_votes()));
        }
    }
    public static  class Gross_Collection_Comparator implements Comparator<Movie>{
        @Override
        public int compare(Movie m1, Movie m2) {
            return Double.compare(Double.parseDouble(m1.getGross()),Double.parseDouble(m2.getGross()));
        }
    }       
}
