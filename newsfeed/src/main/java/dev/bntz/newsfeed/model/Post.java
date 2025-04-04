package dev.bntz.newsfeed.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String id;
    private String title;
    private String slug;
    private String content;
    private LocalDateTime createDate;

    public void generateSlug() {

        if (this.title == null) return;

        this.slug = this.title
                .toLowerCase()
                .replaceAll(" ", "_");
    }
}
