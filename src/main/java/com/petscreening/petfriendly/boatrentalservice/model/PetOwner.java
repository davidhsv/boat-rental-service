package com.petscreening.petfriendly.boatrentalservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet_owner")
public class PetOwner {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pet_owner_id_seq"
    )
    // replace when this is finished https://github.com/vladmihalcea/hypersistence-utils/issues/728
    @GenericGenerator(
            name = "pet_owner_id_seq",
            strategy = "io.hypersistence.utils.hibernate.id.BatchSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence", value = "pet_owner_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "fetch_size", value = "30")
            }
    )
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "contact_info")
    private String contactInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "petOwner", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();
    
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setPetOwner(this);
    }
    
    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setPetOwner(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PetOwner petOwner = (PetOwner) o;
        return getId() != null && Objects.equals(getId(), petOwner.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}