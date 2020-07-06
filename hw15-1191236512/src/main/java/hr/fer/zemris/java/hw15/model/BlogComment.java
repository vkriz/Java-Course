package hr.fer.zemris.java.hw15.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred predstavlja jedan komentar
 * na blog. Sadrži referencu (obostranu vezu) na blog
 * na koji se odnosi, email osobe koja je ostavila
 * komentar, poruku koju je korisnik ostavio te
 * datum ostavljanja komentara.
 * 
 * @author Valentina Križ
 *
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {
	/**
	 * id komentara
	 */
	private Long id;
	
	/**
	 * referenca na blog
	 */
	private BlogEntry blogEntry;
	
	/**
	 * email korisnika
	 */
	private String usersEMail;
	
	/**
	 * komentar
	 */
	private String message;
	
	/**
	 * datum komentiranja
	 */
	private Date postedOn;
	
	/**
	 * Getter za varijablu id
	 * 
	 * @return id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter za varijablu id
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter za varijablu blogEntry
	 * 
	 * @return blogEntry
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Setter za varijablu blogEntry
	 * 
	 * @param blogEntry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter za varijalu usersEmail
	 * 
	 * @return usersEmail
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter za varijablu usersEmail
	 * 
	 * @param usersEMail
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter za varijablu message
	 * 
	 * @return message
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za varijablu message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter za varijablu postedOn
	 * 
	 * @return postedOn
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter za varijablu postedOn
	 * 
	 * @param postedOn
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}