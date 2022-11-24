package auto_deal.center.quant.domain;

import auto_deal.center.user.domain.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="TB_QUANT")
public class Quant {

    @Id @GeneratedValue
    @Column(name="QUANT_ID")
    private Long id;

    @Column
    private String quantType;

    @Column
    private String coinTicker;

    @Column
    private LocalDateTime regdate;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private Users users;

    public void setUser(Users users){
        this.users = users;

        if(!users.getQuants().contains(this)){
            users.getQuants().add(this);
        }
    }
}
