package auto_deal.center.user.domain;

import auto_deal.center.quant.domain.Quant;
import auto_deal.center.talk.domain.Talk;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "TB_USER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    private Long chatId;

    private LocalDateTime regDate;

    @OneToMany(mappedBy = "users")
    @Builder.Default
    private List<Quant> quants = new ArrayList<>();

    @OneToMany(mappedBy = "users")
    @Builder.Default
    private List<Talk> talks = new ArrayList<>();

    public void addQuant(Quant quant){
        this.quants.add(quant);
        
        // 무한루프에 빠지지 않도록 체크
        if(quant.getUsers() != this){
            quant.setUsers(this);
        }
    }

    public void addTalk(Talk talk){
        this.talks.add(talk);

        // 무한루프에 빠지지 않도록 체크
        if(talk.getUsers() != this){
            talk.setUsers(this);
        }
    }

    public void changRegDate(){
        this.regDate = LocalDateTime.now();
    }
}
