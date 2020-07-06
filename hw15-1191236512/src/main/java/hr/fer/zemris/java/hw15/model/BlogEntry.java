package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred predstavlja jedan zapis u bazi
 * koji predstavlja blog. Sadrži informacije
 * o datumu kreiranja, datumu zadnje modifikacije,
 * naslovu, tekstu bloga, autoru i komentarima.
 * 
 * @author Valentina Križ
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {
	/**
	 * Id bloga
	 */
	private Long id;
	
	/**
	 * Komentari
	 */
	private List<BlogComment> comments = new ArrayList<>();
	
	/**
	 * Datum kreiranja
	 */
	private Date createdAt;
	
	/**
	 * Datum zadnje izmjene
	 */
	private Date lastModifiedAt;
	
	/**
	 * Naslov
	 */
	private String title;
	
	/**
	 * Tekst
	 */
	private String text;
	
	/**
	 * Autor
	 */
	private BlogUser creator;
	
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
	 * Getter za varijablu comments
	 * 
	 * @return comments
	 */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Setter za varijablu comments
	 * 
	 * @param comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter za varijablu createrAt
	 * 
	 * @return createdAt
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter za varijablu createdAt
	 * 
	 * @param createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter za varijablu lastModifiedAt
	 * 
	 * @return lastModifiedAt
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter za varijablu lastModifiedAt
	 * 
	 * @param lastModifiedAt
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter za varijablu title
	 * 
	 * @return title
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za varijablu title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za varijablu text
	 * 
	 * @return text
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * Setter za varijablu text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Getter za varijablu creator
	 * 
	 * @return creator
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Setter za varijablu creator
	 * 
	 * @param creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}