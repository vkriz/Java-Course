package hr.fer.zemris.java.hw15.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Razred predstavlja korisnika bloga.
 * Korisnik ima ime, prezime, nadimak,
 * email adresu i lozinku.
 * Svaki korisnik ima jedinstveni nadimak.
 * 
 * @author Valentina Križ
 *
 */
@Entity
@Table(name="blog_users")
public class BlogUser {	
	/**
	 * Identifikator korisnika
	 */
	private Long id;
	
	/**
	 * Nadimak korisnika
	 */
	private String nick;
	
	/**
	 * Ime korisnika
	 */
	private String firstName;
	
	/**
	 * Prezime korisnika
	 */
	private String lastName;
	
	/**
	 * Email adresa korisnika
	 */
	private String email;
	
	/**
	 * Svi blogovi koje je napisao korisnik
	 */
	private List<BlogEntry> blogs;
	
	/**
	 * Lozinka
	 */
	private String passwordHash;
	
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
	 * Getter za varijablu firstName
	 * 
	 * @return firstName
	 */
	@Column(nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter za varijablu firstName
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter za varijablu lastName
	 * 
	 * @return lastName
	 */
	@Column(nullable=false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter za varijablu lastName
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter za varijablu nick
	 * 
	 * @return nick
	 */
	@Column(nullable=false)
	public String getNick() {
		return nick;
	}
	
	/**
	 * Setter za varijablu nick
	 * 
	 * @param nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Getter za varijablu email
	 * 
	 * @return email
	 */
	@Column(nullable=false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter za varijablu email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter za varijablu passwordHash
	 * 
	 * @return passwordHash
	 */
	@Column(nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Setter za varijablu passwordHash
	 * 
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Getter za varijablu blogs
	 * 
	 * @return blogs
	 */
	@OneToMany(mappedBy="creator",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogEntry> getBlogs() {
		return blogs;
	}
	
	/**
	 * Setter za varijablu blogs
	 * 
	 * @param blogs
	 */
	public void setBlogs(List<BlogEntry> blogs) {
		this.blogs = blogs;
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
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}