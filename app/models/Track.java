package models;

import java.util.List;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Track extends Model
{
	
	@Id
	public Long id;
	
	@Required
	public String title;
	
	@Required
	public String artist;
	
	@Required
	public String lyrics;
	
	public static Finder<Long, Track> find = 
			new Finder(Long.class, Track.class);
	
	public static List<Track> all()
	{
		return find.all();
	}
	
	public static void create(Track track)
	{
		track.save();
	}
	
	public static void delete(Long id)
	{
		find.ref(id).delete();
	}
	
}
