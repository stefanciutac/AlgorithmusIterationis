import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main
{
    public static void displayBanner()
    {
        System.out.println("                                                                                                                                                          \n" +
                "   ▄▄   ▀▀█                           ▀      ▄    █                           ▄▄▄▄▄    ▄                           ▄      ▀                    ▀          \n" +
                "   ██     █     ▄▄▄▄   ▄▄▄    ▄ ▄▄  ▄▄▄    ▄▄█▄▄  █ ▄▄   ▄▄▄▄▄  ▄   ▄   ▄▄▄     █    ▄▄█▄▄   ▄▄▄    ▄ ▄▄   ▄▄▄   ▄▄█▄▄  ▄▄▄     ▄▄▄   ▄ ▄▄   ▄▄▄     ▄▄▄  \n" +
                "  █  █    █    █▀ ▀█  █▀ ▀█   █▀  ▀   █      █    █▀  █  █ █ █  █   █  █   ▀    █      █    █▀  █   █▀  ▀ ▀   █    █      █    █▀ ▀█  █▀  █    █    █   ▀ \n" +
                "  █▄▄█    █    █   █  █   █   █       █      █    █   █  █ █ █  █   █   ▀▀▀▄    █      █    █▀▀▀▀   █     ▄▀▀▀█    █      █    █   █  █   █    █     ▀▀▀▄ \n" +
                " █    █   ▀▄▄  ▀█▄▀█  ▀█▄█▀   █     ▄▄█▄▄    ▀▄▄  █   █  █ █ █  ▀▄▄▀█  ▀▄▄▄▀  ▄▄█▄▄    ▀▄▄  ▀█▄▄▀   █     ▀▄▄▀█    ▀▄▄  ▄▄█▄▄  ▀█▄█▀  █   █  ▄▄█▄▄  ▀▄▄▄▀ \n" +
                "                ▄  █                                                                                                                                      \n" +
                "                 ▀▀                                                                                                                                       ");
    }

    public static void displayCommands()
    {
        System.out.println("[Database Commands]\n" +
                ":q\tquit program or cancel current review/modification\n" +
                ":w\twrite changes to disk; save\n" +
                ":r\treview queue\n" +
                ":a\tadd error to database\n" +
                ":h\thelp; display this list\n" +
                "ls\tlist database; display the database\n" +
                "\n[Queue-Buffer Commands]\n" +
                "lq\tlist queue; display the queue\n" +
                "lb\tlist buffer; display the buffer\n" +
                "fn <tag><tag>...<tag>\tfilter intersection; filter buffer by tag using boolean AND\n" +
                "fu <tag><tag>...<tag>\tfilter union; filter buffer by tag using boolean OR\n" +
                "\nu\tqueue = (queue) union (buffer)\n" +
                "n\tqueue = (queue) intersection (buffer)\n" +
                "ld\tload; overwrites queue with buffer\n" +
                "\nm <multiplier>\tmultiply; set the urgency multiplier to items in buffer\n" +
                "gf <review duration>\tgreedy-fill; uses an algorithm to greedy-filter buffer\n" +
                "rm\tremove errors in buffer from database\n" +
                "rst\treset buffer to entire database\n");
    }

    public static void displayList(ArrayList<Error> list)
    {
        for (int i = 0; i < list.size(); i ++)
        {
            System.out.println("(" + (i + 1) + ")\t" + list.get(i).getTitle() + "\t(" + list.get(i).getTags() + ")");
        }
    }

    public static ArrayList<Error> filterOr(ArrayList<Error> list, ArrayList<String> tagList)
    {
        System.out.println("[Filtering OR]");

        ArrayList<Error> newList = new ArrayList<>();

        // add all error objects that match at least one tag
        for (Error error: list)
        {
            for (String tag: tagList)
            {
                if (error.getTags().contains(tag))
                {
                    newList.add(error);
                    break;
                }
            }
        }

        return newList;
    }

    public static ArrayList<Error> filterAnd(ArrayList<Error> list, ArrayList<String> tagList)
    {
        System.out.println("[Filtering AND]");

        ArrayList<Error> newList = new ArrayList<>();

        // add only error objects that match all the tags
        for (Error error: list)
        {
            boolean valid = true;
            for (String tag: tagList)
            {
                if (!error.getTags().contains(tag))
                {
                    valid = false;
                    break;
                }
            }

            if (valid)
            {
                newList.add(error);
            }
        }

        return newList;
    }

    // returns empty list if no args provided, handling the edge case of filtering by no tags
    public static ArrayList<String> pacmanArgs(Scanner sc)
    {
        ArrayList<String> argsList = new ArrayList<String>();

        String args = sc.nextLine();

        Scanner stringSc = new Scanner(args);

        while (stringSc.hasNext())
        {
            argsList.add(stringSc.next());
        }

        return argsList;
    }

    public static String getNext(Scanner sc)
    {
        try
        {
            return sc.next();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Exception: sc.next() is being called on an empty buffer");
        }
        return "";
    }

    public static ArrayList<Error> listUnion(ArrayList<Error> listA, ArrayList<Error> listB)
    {
        ArrayList<Error> union = new ArrayList<>();

        // add duplicates and A-unique
        for (Error errorA: listA)
        {
            boolean foundMatch = false;
            for (Error errorB: listB)
            {
                if (errorA.equals(errorB))
                {
                    union.add(errorA);
                    foundMatch = true;
                }
            }

            if (!foundMatch)
            {
                union.add(errorA);
            }
        }

        // add B-unique
        for (Error errorB: listB)
        {
            boolean foundMatch = false;
            for (Error errorA: listA)
            {
                if (errorA.equals(errorB))
                {
                    foundMatch = true;
                }
            }

            if (!foundMatch)
            {
                union.add(errorB);
            }
        }

        return union;
    }

    public static ArrayList<Error> listIntersection(ArrayList<Error> listA, ArrayList<Error> listB)
    {
        ArrayList<Error> intersection = new ArrayList<>();

        for (Error errorA: listA)
        {
            for (Error errorB: listB)
            {
                if (errorA.equals(errorB))
                {
                    intersection.add(errorA);
                    break;
                }
            }
        }

        return intersection;
    }

    public static ArrayList<Error> applyMultiplier(ArrayList<Error> list, double multiplier)
    {
        // if any of the tags in tagList are present, update the multiplier
        for (Error error: list)
        {
            error.setUrgencyMultiplier(multiplier);
            System.out.println("[Added multiplier: " + error.getUrgencyMultiplier() + "]\t" + error.getTitle());
            break;
        }
        return list;
    }

    // recursive function for taking in a bounded index or :q
    public static String validateNum(int max, String message, Scanner sc)
    {
        System.out.print(message);
        String choice = getNext(sc);

        if (!choice.equals(":q"))
        {
            int index;
            try
            {
                index = Integer.parseInt(choice);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Exception: Must be an integer value.");
                choice = validateNum(max, message, sc);
            }

            if (!choice.equals(":q"))
            {
                index = Integer.parseInt(choice);

                if (index > max)
                {
                    System.out.println("Exception: Must not exceed " + max + ".");
                    choice = validateNum(max, message, sc);
                }
            }
        }

        return choice;
    }

    public static Review reviewError(Error error, Scanner sc) throws QuitException
    {
        Review review = new Review();

        System.out.println("\n[Review started]");
        System.out.println("[Title]: " + error.getTitle());
        System.out.println("[Pointer]: " + error.getPointer());

        System.out.print("[Enter anything to continue]: ");
        if (sc.next().equals(":q"))
        {
            throw new QuitException();
        }

        System.out.println("[Error]: " + error.getDescription());
        String input = validateNum(100, "[Enter confidence]: ", sc);
        if (input.equals(":q"))
        {
            throw new QuitException();
        }
        else
        {
            review.setConfidence(Integer.parseInt(input));
        }

        System.out.println("[Review complete]");

        return review;
    }

    public static ArrayList<Error> mergeError(Error error, ArrayList<Error> database)
    {
        for (Error errorD: database)
            {
                if (error.equals(errorD))
                {
                    errorD = error;
                }
            }

        return database;
    }

    public static ArrayList<Error> reviewQueue(ArrayList<Error> queue, Scanner sc, ArrayList<Error> database)
    {
        System.out.println("\n[Queue]");

        displayList(queue);

        String choice = validateNum(queue.size(), "\n[Enter no. of item to review]: ", sc);

        while (!choice.equals(":q"))
        {
            int index = Integer.parseInt(choice) - 1;

            try
            {
                queue.get(index).addReview(reviewError(queue.get(index), sc));
                database = mergeError(queue.get(index), database);
                saveData(database);
            }
            catch (QuitException e)
            {
                System.out.println("[Review cancelled]");
            }

            choice = validateNum(queue.size(), "[Enter no. of item to review]: ", sc);
        }
        System.out.println("[Review session ended]");

        return database;
    }

    public static Error addError() throws QuitException
    {
        Scanner sc = new Scanner(System.in);

        // get title
        System.out.print("[Enter title]: ");
        //sc.nextLine();  // clear buffer
        String title = sc.nextLine();

        if (title.equals(":q"))
        {
            throw new QuitException();
        }

        // get description
        System.out.print("[Enter description]: ");
        String description = sc.nextLine();

        if (description.equals(":q"))
        {
            throw new QuitException();
        }

        // get pointer
        System.out.print("[Enter pointer]: ");
        String pointer = sc.nextLine();

        if (pointer.equals(":q"))
        {
            throw new QuitException();
        }

        // populate initial "review"
        Review review = new Review();
        String initialConfidence = validateNum(100, "[Enter confidence]: ", sc);
        if (initialConfidence.equals(":q"))
        {
            throw new QuitException();
        }
        review.setConfidence(Double.parseDouble(initialConfidence));

        // get estimatedDuration
        String estimatedDurationString = validateNum(600, "[Enter estimated duration/mins]: ", sc);

        if (estimatedDurationString.equals(":q"))
        {
            throw new QuitException();
        }

        int estimatedDuration = Integer.parseInt(estimatedDurationString);


        // get tags
        sc.nextLine();  // clear buffer
        System.out.print("[Enter tags]: ");
        String tags = sc.nextLine();

        if (tags.equals(":q"))
        {
            throw new QuitException();
        }

        // construct and return new Error object
        Error error = new Error(title, description, pointer, tags, estimatedDuration);
        error.addReview(review);
        return error;
    }

    public static ArrayList<Error> insertionSort(ArrayList<Error> list)
    {
        for (int i = 1; i < list.size(); i ++)
        {
            int newIndex = i;
            while (newIndex > 0 && list.get(newIndex).calculateScore() > list.get(newIndex - 1).calculateScore())
            {
                // swap elements
                Error tmp = list.get(newIndex);
                list.set(newIndex, list.get(newIndex - 1));
                list.set(newIndex - 1, tmp);
                newIndex --;
            }
        }

        return list;
    }

    public static ArrayList<Error> greedyFill(ArrayList<Error> buffer, int maxTime)
    {
        buffer = insertionSort(buffer);
        ArrayList<Error> queue = new ArrayList<>();

        for (Error error: buffer)
        {
            if (maxTime < 1)
            {
                break;
            }
            else if (error.getEstimatedDuration() <= maxTime)
            {
                queue.add(error);
                maxTime -= error.getEstimatedDuration();
            }
        }

        return queue;
    }

    // temporary: for testing purposes
    public static ArrayList<Error> getTestErrors(int count)
    {
        ArrayList<Error> errors = new ArrayList<>();

        for (int i = 1; i <= count; i ++)
        {
            String title = "error" + i;
            String description = "you made mistake no." + i;
            String pointer = "textbook chapter " + i;
            int estimatedDuration = i;
            String tags = "";
            for (int j = 1; j < i; j ++)
            {
                tags += "t" + j + ", ";
            }
            tags += "t" + i;

            Error error = new Error(title, description, pointer, tags, estimatedDuration);
            error.addReview(new Review(count - i));

            errors.add(error);
        }

        return errors;
    }

    public static ArrayList<Error> openFile() throws IOException
    {
        ArrayList<Error> database = new ArrayList<>();

        Gson gson = new Gson();

        try
        {
            File file = new File("algorithmusIterationisData.json");
            Scanner fileReader = new Scanner(file);

            String data = "";

            while (fileReader.hasNextLine())
            {
                data += fileReader.nextLine() + "\n";
            }

            try
            {
                // typeToken is neccessary because it retains generic type information; gson converts Error to a hashmap otherwise
                Type typeToken = new TypeToken<ArrayList<Error>>(){}.getType();
                database = gson.fromJson(data, typeToken);
                System.out.println("[Database opened successfully]");
            }
            catch (JsonSyntaxException e)
            {
                throw new JsonSyntaxException("[Could not parse .json file]");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[Exception: File not found.]");
            throw new IOException();
        }
        catch (JsonSyntaxException e)
        {
            System.out.println(e.getMessage());
            throw new IOException();
        }

        return database;
    }

    public static ArrayList<Error> updateForgettingCurves(ArrayList<Error> database)
    {
        for (Error error: database)
        {
            error.updateConfidence();
        }

        return database;
    }

    public static ArrayList<Error> resetMultipliers(ArrayList<Error> database)
    {
        for (Error error: database)
        {
            error.setUrgencyMultiplier(1.0);
        }

        return database;
    }

    public static void saveData(ArrayList<Error> database)
    {
        Gson gson = new Gson();
        String db = gson.toJson(resetMultipliers(database));

        try
        {
            File data = new File("algorithmusIterationisData.json");
            FileWriter fileWriter = new FileWriter(data);
            fileWriter.write(db);
            fileWriter.close();
            System.out.println("[File saved successfully.]");
        }
        catch (IOException e)
        {
            System.out.println("[Exception: Error occurred while saving file.]");
        }
    }

    public static void quit()
    {
        System.out.println("AlgorithmusIterationis exited.");
    }

    public static void main()
    {
        ArrayList<Error> database = new ArrayList<>();  // construct using json file parser
        ArrayList<Error> queue = new ArrayList<>();
        ArrayList<Error> buffer = new ArrayList<>();

        // display welcome screen
        displayBanner();
        displayCommands();

        // file opening
        boolean isFileOpened = true;

        try
        {
            database = openFile();
        }
        catch (IOException e)
        {
            isFileOpened = false;
        }

        if (isFileOpened)
        {
            database = updateForgettingCurves(database);
            buffer = new ArrayList<>(database);

            Scanner sc = new Scanner(System.in);
            String command;

            // main command loop
            do
            {
                System.out.print("\n[cmd]: ");
                command = sc.next();


                // check for permanent or program commands
                if (command.equals(":r"))
                {
                    database = reviewQueue(queue, sc, database);
                }
                else if (command.equals(":w"))
                {
                    saveData(database);
                }
                else if (command.equals(":h"))
                {
                    displayCommands();
                }
                else if (command.equals(":a"))
                {
                    try
                    {
                        database.add(addError());
                        saveData(database);
                    }
                    catch (QuitException e)
                    {
                        System.out.println("[Add error operation cancelled]");
                    }
                }
                else if (command.equals(":q"))
                {
                    System.out.print("[Exit program? y/N]: ");
                    String choice = sc.next();
                    if (!choice.equals("y"))
                    {
                        command = "";
                    }
                }
                // check for session commands
                else if (command.equals("ls"))
                {
                    displayList(database);
                }
                else if (command.equals("lq"))
                {
                    displayList(queue);
                }
                else if (command.equals("lb"))
                {
                    displayList(buffer);
                }
                else if (command.equals("fn"))
                {
                    ArrayList<String> tagList = pacmanArgs(sc);
                    buffer = filterAnd(buffer, tagList);
                }
                else if (command.equals("fu"))
                {
                    ArrayList<String> tagList = pacmanArgs(sc);
                    buffer = filterOr(buffer, tagList);
                }
                else if (command.equals("u"))
                {
                    queue = listUnion(queue, buffer);
                }
                else if (command.equals("n"))
                {
                    queue = listIntersection(queue, buffer);
                }
                else if (command.equals("ld"))
                {
                    queue = new ArrayList<>(buffer);
                }
                else if (command.equals("m"))
                {
                    double multiplier = 1.0;
                    boolean isValidMultiplier = true;

                    try
                    {
                        multiplier = Double.parseDouble(getNext(sc));
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Exception: multiplier must be a number");
                        isValidMultiplier = false;
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println("Exception: you must pass the arguments <multiplier><tag> to command m");
                        isValidMultiplier = false;
                    }

                    if (isValidMultiplier)
                    {
                        buffer = applyMultiplier(buffer, multiplier);
                    }

                }
                else if (command.equals("gf"))
                {
                    int minutesFree = Integer.parseInt(validateNum(1440, "[Enter review duration/mins]: ", sc));

                    buffer = greedyFill(buffer, minutesFree);
                }
                else if (command.equals("rst"))
                {
                    buffer = new ArrayList<>(database);
                }
                else if (command.equals("rm"))
                {
                    for (Error errorB: buffer)
                    {
                        for (Error errorDB: database)
                        {
                            if (errorB.equals(errorDB))
                            {
                                database.remove(errorDB);
                            }
                        }
                    }

                    buffer.clear();
                }
                else
                {
                    System.out.println("Invalid command. Type :h to display list of commands.");
                }

            } while (!command.equals(":q"));
        }

        // quit the application
        saveData(database);
        quit();
    }
}