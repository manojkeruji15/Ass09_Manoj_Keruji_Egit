package assignment09_Manoj_Keruji;

import java.util.*;
import java.sql.Date;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {

	List <Movie> mList=new ArrayList<Movie>();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("========================= Movie Details Program=======================");
		Main m=new Main();
		
		System.out.println("\n ******** Adding  movies to List ***********");
		m.mList=m.populateMovies();
		for (Movie  n : m.mList)
		{System.out.println("list obj :"+n.toString());}
		
		System.out.println("\n ******** Adding all the movies to Database ***********");
		//m.allAllMoviesInDb(mList);
		 
		System.out.println("\n ******** Add new  movie to List ***********");
		Date date2=Date.valueOf("2020-02-02");
		List <String> al=new ArrayList<String>();
		al.add("ranbir kapoor");
		Movie m2=new Movie (9,"ek dil","Romanti","Eng",date2,al,4.9d,324244d);
		m.addMovie(m2,m.mList);
		for (Movie  n : m.mList)
		{System.out.println("list obj :"+n.toString());}
		
		System.out.println("\n ******** Serialize movies to listObj.txt file ***********");
		m.serializeMovies(m.mList,"listObj.txt");
		
		System.out.println("\n ******** Deserialize movies from listObj.txt file ***********");
		List <Movie> newlist=m.deserializeMovie("listObj.txt");
		for (Movie mo:newlist)
		{
			System.out.println(mo.toString());
		}
		
		System.out.println("\n ******** GetMovie released in year 2020***********");
		newlist=m.getMoviesRealeasedInYear(2020);
		for (Movie mo:newlist)
		{
			System.out.println("in 2020 ==>"+mo.toString());
		}
		
		System.out.println("\n ******** Get Movies by Actor name : amir Khan and John Abrahim***********");
		newlist=m.getMoviesByActor("amir khan","john abrahim");
		for (Movie mo:newlist)
		{
			System.out.println("in 2021 ==>"+mo.toString());
		}
		
		System.out.println("\n ******** UPdate Movie rating and Total bussinessDone for movie Ek-Dil ***********");
		m.updateRatings(m2,2.2,m.mList);
		m.updateBusiness(m2,200000,m.mList);
		for (Movie mo: m.mList)
		{
			if(mo.equals(m2))
			{
			System.out.println("in updated ==>"+mo.toString());
			}
		}
		
		//Set<Movie> MovieSet=m.businessDone(47000d);
		System.out.println("\n ******** Get Movies in HashMap by Language and sorted by total bussiness done in decending order in SET<Mvoie>***********");
		Map<String,Set<Movie>> map=new HashMap<String,Set<Movie>>();
		map=m.businessDone(47000d);
		
		
	}
	
	public List<Movie> populateMovies(){
		File file=new File("C:\\Users\\Manoj\\eclipse-workspace\\assignment09_Manoj_Keruji\\MovieDetails.txt");    //creates a new file instance  
        FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   //reads the file  
        BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
        String sb;  
        String line;  
        List<Movie> mList=new ArrayList<Movie>();
        try {
			while((line=br.readLine())!=null)  
			{  
				
			sb=line;
			int i=sb.indexOf(",");
			String ids=sb.substring(0,i);
			
			int id= Integer.parseInt(ids);  
			
			int j=sb.indexOf(",",i+1);
			String name=sb.substring(i+1, j);
			i=j;
			j=sb.indexOf(",",i+1);
			String movieType=sb.substring(i+1, j);
			i=j;
			j=sb.indexOf(",",i+1);
			String lang=sb.substring(i+1, j);
			i=j;
			j=sb.indexOf(",",i+1);
					
			String dt=sb.substring(i+1,j);
			
			Date DateR=Date.valueOf(dt);
			
			i=j;
			j=sb.indexOf(",",i+1);
			String Actor=sb.substring(i+1, j);
			List<String> ActorList=new ArrayList<String>();
			ActorList.add(Actor);
			i=j;
			j=sb.indexOf(",",i+1);
			double rating=Double.parseDouble(sb.substring(i+1, j));  
			i=j;
			
			double Bussiness=Double.parseDouble(sb.substring(i+1 ));  
			Movie m=new Movie(id,name,movieType,lang,DateR,ActorList,rating,Bussiness);
			mList.add(m);
			
      
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mList;  
	}
	
	@SuppressWarnings("resource")
	Boolean allAllMoviesInDb(List<Movie> movies) throws Exception {
		//Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/manoj","root","root");  
		//here sonoo is database name, root is username and password 
		try {
		for(Movie m:movies)
		{
		String query = " insert into moviedetails values(?,?,?,?,?,?,?)";
		Date  DateRR=Date.valueOf(m.getReleaseDate().toString());
		
		        PreparedStatement preparedStmt = con.prepareStatement(query);
		        preparedStmt.setInt    (1,  m.getMovieId());
		        preparedStmt.setString (2, m.getMovieName());
		        preparedStmt.setString (3, m.getMovieType());
		        preparedStmt.setDate (4, DateRR);
		        preparedStmt.setString (5, m.getLanguage());
		       
		        preparedStmt.setDouble(6, m.getRating());
		        preparedStmt.setDouble(7, m.getTotalBusinessDone());
		        preparedStmt.execute();
		        
		        for (String s:m.getCasting())
		        {
		        	query = "insert into castm values(?,?)";  
		        	 preparedStmt = con.prepareStatement(query);
		        			        
		        	  preparedStmt.setInt(1,m.getMovieId());
			          preparedStmt.setString(2, s);
		        	 
		  		      preparedStmt.execute();
		  		      preparedStmt.close();
		        }
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
	
	public void addMovie(Movie m,List<Movie> movie) {
		movie.add(m);
	}
	
	public void serializeMovies(List<Movie> movies, String fileName)
	{
		 try
	        {
			 	File f=new File(fileName);
			 	if(!f.exists())
			 	{
			 		f.createNewFile();
			 	}
	            FileOutputStream fos = new FileOutputStream(f);
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(movies);
	            oos.close();
	            fos.close();
	            System.out.println("Done");
	        } 
	        catch (IOException ioe) 
	        {
	            ioe.printStackTrace();
	        }
	}
	
	@SuppressWarnings("unchecked")
	List<Movie> deserializeMovie(String filename)
	{
		ArrayList<Movie> namesList = new ArrayList<Movie>();
        
        try
        {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            namesList = (ArrayList<Movie>) ois.readObject();
 
            ois.close();
            fis.close();
        } 
        catch(Exception e)
        {
        	System.out.println(e);
        }
         
        //Verify list data
        return namesList;
	}
	
	List<Movie> getMoviesRealeasedInYear(int year)
	{
		List <Movie> mList=new ArrayList<Movie>();
		for (Movie mon: this.mList)
		{
			int y=Integer.parseInt(mon.getReleaseDate().toString().substring(0,4));
			if(y==year)
			{
				mList.add(mon);
			}
		}
		return mList;
	}
	
	List<Movie> getMoviesByActor(String... actorNames){
		List <Movie> mList=new ArrayList<Movie>();
		for (Movie mon: this.mList)
		{
			for (String n:mon.getCasting())
			{
				
				if(Arrays.asList(actorNames).contains(n))
				{
					mList.add(mon);
					break;
				}
			}
		}
		return mList;
	}
	
	Void updateRatings(Movie movie, double rating ,List<Movie> movies)
	{
		for (Movie m:movies)
		{
			if(m.equals(movie))
			{
				m.setRating(rating);
			}
		}
		return null;
	}
	
	Void updateBusiness(Movie movie, double amount,List<Movie> movies)
	{
		for (Movie m:movies)
		{
			if(m.equals(movie))
			{
				m.setTotalBusinessDone(amount);
			}
		}
		return null;
	}
	Map<String,Set<Movie>> businessDone(double amount)
	{
		Set<Movie> bus=new TreeSet<Movie>();
		Map<String,Set<Movie>> map=new HashMap<String,Set<Movie>>();
		for (Movie m:this.mList)
		{
			if(m.getTotalBusinessDone()>amount)
			{
				
				bus.add(m);
				if(map.containsKey(m.getLanguage()))
				{
					map.get(m.getLanguage()).add(m);
				}
				else {
				Set<Movie> bus2=new TreeSet<Movie>();
				bus2.add(m);
				map.put(m.getLanguage(),bus2);	
				}
			}
			
		}
		
		 for(Map.Entry m : map.entrySet()){    
			    System.out.println(m.getKey()+" "+m.getValue());    
			   }  
		//Collections.sort(bus,new Sortamout());
		return map;
		
		
	}
	
	
	
}


class Movie implements Serializable,Comparable<Movie>{
	private int movieId;
	private String movieName,movieType, language;
	private Date releaseDate;
	private List<String> casting;
	private Double rating, totalBusinessDone;
	
	public Movie(int movieId, String movieName, String movieType, String language, Date releaseDate,
			List<String> casting, Double rating, Double totalBusinessDone) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.movieType = movieType;
		this.language = language;
		this.releaseDate = releaseDate;
		this.casting = casting;
		this.rating = rating;
		this.totalBusinessDone = totalBusinessDone;
	}
	
	
	public Double gettotabussinessDoene() {
		// TODO Auto-generated method stub
		return null;
	}


	public int getMovieId() {
		return movieId;
	}
	
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getMovieType() {
		return movieType;
	}
	public void setMovieType(String movieType) {
		this.movieType = movieType;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public List<String> getCasting() {
	return casting;}
	public void setCasting(List<String> casting) {
	this.casting = casting;}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Double getTotalBusinessDone() {
		return totalBusinessDone;
	}
	public void setTotalBusinessDone(Double totalBusinessDone) {
		this.totalBusinessDone = totalBusinessDone;
	}
	
	@Override
	public String toString() {
		return "\nMovie [movieId=" + this.getMovieId() + ", movieName=" +  this.getMovieName() + ", movieType=" +  this.getMovieType() + ", language="
				+  this.getLanguage() + ", releaseDate=" +  this.getReleaseDate() + ", casting=" +  this.getCasting() + ", rating=" +  this.getRating()
				+ ", totalBusinessDone=" +  this.getTotalBusinessDone() + "]";
	}
	
	  public int compareTo(Movie h1) {
	        return (-1)*this.getTotalBusinessDone().compareTo(h1.getTotalBusinessDone());
	    } 
	

}