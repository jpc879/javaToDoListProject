package de.hdm_stuttgart.toDoList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;

public class UserTest {

    private Main main = new Main();
    private User testuser;
    private int lines;

    @Before
    public void setUp() throws Exception {
        lines = getNumberOfLines();
        main.AddUser("Bob", "1234");
    }

    // Helper 1
    private int getNumberOfLines() throws Exception {
        Scanner file = new Scanner(new File("src/userdata/user.txt"));
        int n = 0;
        while (file.hasNextLine()) {
            n++;
            file.nextLine();
        }
        return n;
    }

    // Helper 2
    private void DeleteUser(String name) {
        File f = new File("src/userdata/user.txt");
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            for(int i = 0;i<lines.size()-1;i++) {
                if(lines.get(i).equals("U :" + name)) {
                    lines.remove(i);
                    for(int j = i;j< lines.size();j++) {
                        if(lines.get(j).equals("P :1234")) {
                            lines.remove(j);
                            if(lines.size()== j) {
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            Files.write(f.toPath(), lines);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Test 1
    @Test
    public void  testUser() {
        testuser = new User("Bob", "1234");
        assertEquals("Bob", testuser.getUserName());
    }


    // Test 2: Neuer User wird angelegt
    @Test
    public void testAddUser() throws Exception {
        assertEquals(lines+2, getNumberOfLines());
    }


    // Test 3: User Login
    @Test
    public void testCheckUser() {
        assertTrue(main.CheckUser("Bob"));
    }


    // Test 4: User Login 2
    @Test
    public void testCheckUser2() {
        assertTrue(main.CheckUser("Bob", "1234"));
    }


    // Test 5: User Login nicht möglich
    @Test
    public void testCheckUserNegative() {
        assertFalse(main.CheckUser("Nutzer"));
    }


    // Test 6: User Login nicht möglich (2)
    @Test
    public void testCheckUserNegative2() {
        assertFalse(main.CheckUser("Nutzer", "555"));
    }


    @After
    public void tearDown() {
        DeleteUser("Bob");
        testuser = null;
        lines = 0;
    }
}
