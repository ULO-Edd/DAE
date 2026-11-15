package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;

import java.util.Arrays;
import java.util.LinkedList;

@Startup
@Singleton
public class ConfigBean {

    @EJB private TagBean tagBean;
    @EJB private PublicationBean publicationBean;
    @EJB private UserBean userBean; 

    @PostConstruct
    public void populateDB() {
        System.out.println("Populating database with initial data...");

        // 1. Criar Utilizadores
        try {
            userBean.create("Colab 1", "colab1@mail.pt", "colab1", Role.COLLABORATOR);
            userBean.create("Colab 2", "colab2@mail.pt", "colab2", Role.COLLABORATOR);
            userBean.create("Mod 1", "mod1@mail.pt", "mod1", Role.MODERATOR);
            userBean.create("Admin 1", "admin1@mail.pt", "admin1", Role.ADMINISTRATOR);
        } catch (Exception e) {
            System.out.println("Users likely already exist...");
        }

        // 2. Criar Tags
        try {
            tagBean.create("Software Engineering");
            tagBean.create("Data Science");
            tagBean.create("AI");
            tagBean.create("Cybersecurity");
            tagBean.create("Networking");
            tagBean.create("Databases");
        } catch (Exception e) {
            System.out.println("Tags likely already exist...");
        }

        // 3. Recuperar as Entidades
        User colab1 = userBean.find("colab1@mail.pt");
        Tag tagSoftEng = tagBean.find("Software Engineering");
        Tag tagAI = tagBean.find("AI");
        Tag tagData = tagBean.find("Data Science");
        

        // 4. Criar Publicações Mock
        if (colab1 != null) {
            
            // --- Publicação 1 ---
            LinkedList<String> authors1 = new LinkedList<>(Arrays.asList("João Silva", "Maria Santos"));
            LinkedList<Tag> tags1 = new LinkedList<>();
            if (tagSoftEng != null) tags1.add(tagSoftEng);
            if (tagAI != null) tags1.add(tagAI);

            publicationBean.create(
                colab1,                         
                "Impact of AI in Software Dev", 
                authors1,                       
                "dummy_files/ai_paper.pdf",    
                FileType.CONFERENCE_PAPER,
                "This paper explores how LLMs are changing coding...", 
                tags1                           
            );

            // --- Publicação 2 ---
            LinkedList<String> authors2 = new LinkedList<>(Arrays.asList("Colab 1", "Albert Einstein"));
            LinkedList<Tag> tags2 = new LinkedList<>();
            if (tagData != null) tags2.add(tagData);

            publicationBean.create(
                colab1,
                "Big Data Analysis in 2025",
                authors2,
                "dummy_files/data_analysis.pdf",
                FileType.CONFERENCE_PAPER,
                "A comprehensive study on big data trends.",
                tags2
            );
            
            System.out.println("Mock Publications created successfully.");
        }

        System.out.println("Database populated with initial data.");
    }
}
