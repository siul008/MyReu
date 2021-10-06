package com.example.reunion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.reunion.model.Reunion;
import com.example.reunion.service.ReunionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReunionServiceTest {
    private ReunionRepository reuRepo;

    @Before
    public void setup()
    { //On instancie le Repository
        reuRepo = ReunionRepository.getInstance();
        reuRepo.clearReunions();
    }
    @Test
    public void createReunionWithSucess()
    { //On créer une réunion et on vérifie que la liste des réunion contient une réunion, et qu'elle correspond
        //Given
        List<String> participants = new ArrayList<>();
        participants.add("test");
        Reunion reunion = new Reunion(1,"test","test",1,1,"30","6",participants);

        //When
        reuRepo.createReunion(reunion);
        List<Reunion> reunionList = reuRepo.getReunions();

        //Then
        assertEquals(1,reunionList.size());
        assert reunionList.contains(reunion);
    }

    @Test public void deleteReunionWithSucess()
    { // On crée une reunion et la supprime, et verifie que la liste ne contient pas cette réunion
        //Given
        List<String> participants = new ArrayList<>();
        participants.add("test");
        Reunion reunion = new Reunion(1,"test","test",1,1,"30","6",participants);
        reuRepo.createReunion(reunion);
        List<Reunion> reunionList = reuRepo.getReunions();


        //When
        reuRepo.deleteReunion(reunion);

        //Then
        assertFalse(reunionList.contains(reunion));
    }

    @Test public void checkIfAvailableRoom1()
    { //On créer un reunion x et on vérifie que reuRepo.checkForAvailableRoom empeche une reunion d'etre crée dans sa plage horaire
        //Given
        List<String> participants = new ArrayList<>();
        participants.add("test");
        Reunion reunion = new Reunion(1,"test","Salle 1",10,30,"30","6",participants);
        reuRepo.createReunion(reunion);

        //When
        boolean result = reuRepo.checkForAvailableRoom(10, 45, "30", "Salle 1");

        //Then

        assertFalse(result);


    }
    @Test public void checkIfAvailableRoom2()
    { //On créer un reunion x et on vérifie que reuRepo.checkForAvailableRoom empeche une reunion d'etre crée dans sa plage horaire
        //Given
        List<String> participants = new ArrayList<>();
        participants.add("test");
        Reunion reunion = new Reunion(1,"test","Salle 1",10,30,"30","6",participants);
        reuRepo.createReunion(reunion);

        //When
        boolean result = reuRepo.checkForAvailableRoom(12, 0, "30", "Salle 1");

        //Then
        assertTrue(result);
    }
    @Test public void checkIfAvailableRoom3()
    { //On créer un reunion x et on vérifie que reuRepo.checkForAvailableRoom empeche une reunion d'etre crée dans sa plage horaire
        //Given
        List<String> participants = new ArrayList<>();
        participants.add("test");
        Reunion reunion = new Reunion(1,"test","Salle 1",10,30,"30","6",participants);
        reuRepo.createReunion(reunion);

        //When
        boolean result = reuRepo.checkForAvailableRoom(10, 40, "30", "Salle 2");

        //Then
        assertTrue(result);
    }
    @Test public void checkIfFilteringSucessfully()
    { //On crée des réunions puis verifie que chaque liste filtré correspond aux critères
        //Given
        List<String> participants = new ArrayList<>();
        List<Reunion> filteredList1 = new ArrayList<>();
        List<Reunion> filteredList2 = new ArrayList<>();
        List<Reunion> filteredList3 = new ArrayList<>();
        List<Reunion> filteredList4 = new ArrayList<>();
        List<Reunion> filteredList5 = new ArrayList<>();

        participants.add("test");
        Reunion reunion1 = new Reunion(1,"test","Salle 1",10,30,"30","6",participants);
        reuRepo.createReunion(reunion1);
        Reunion reunion2 = new Reunion(2,"test","Salle 1",12,30,"30","6",participants);
        reuRepo.createReunion(reunion2);
        Reunion reunion3 = new Reunion(3,"test","Salle 2",10,30,"30","6",participants);
        reuRepo.createReunion(reunion3);

        //When
        filteredList1 = reuRepo.filterReunion(10,null,null);
        filteredList2 = reuRepo.filterReunion(10,12,null);
        filteredList3 = reuRepo.filterReunion(null,null,"Salle 1");
        filteredList4 = reuRepo.filterReunion(null,null,null);
        filteredList5 = reuRepo.filterReunion(12,13,"Salle 1");

        //Then
        assert filteredList1.contains(reunion1);
        assert filteredList1.contains(reunion2);
        assert filteredList1.contains(reunion3);

        assert filteredList2.contains(reunion1);
        assert filteredList2.contains(reunion3);

        assert filteredList3.contains(reunion1);
        assert filteredList3.contains(reunion2);

        assert filteredList4.isEmpty();

        assert filteredList5.contains(reunion2);
    }
}