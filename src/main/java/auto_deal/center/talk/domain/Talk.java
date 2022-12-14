package auto_deal.center.talk.domain;

import auto_deal.center.user.domain.Users;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_TALK")
@Entity
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TALK_ID")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="USER_ID")
    private Users users;

    private LocalDateTime regDate;

    public void setUser(Users users){
        this.users = users;

        if(!users.getTalks().contains(this)){
            users.getTalks().add(this);
        }
    }
}
