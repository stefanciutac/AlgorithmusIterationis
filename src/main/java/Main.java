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
                ":r\treview queue\n" +
                ":a\tadd error to database" +
                ":h\tdisplay this list\n" +
                ":ddb\tdisplay the database\n" +
                "\n[Queue-Buffer]\n" +
                "dq\tdisplay the queue\n" +
                "db\tdisplay the buffer\n" +
                "fn <tag><tag>...<tag>\tfilter buffer by tag using boolean AND\n" +
                "fu <tag><tag>...<tag>\tfilter buffer by tag using boolean OR\n" +
                "\nu\tqueue = (queue) union (buffer)\n" +
                "n\tqueue = (queue) intersection (buffer)\n" +
                "ld\toverwrites queue with buffer\n" +
                "\nm <multiplier><tag><tag>...<tag>\tset the urgency multiplier for given tags\n" +
                "gf <review duration>\tuses an algorithm to greedy-filter database" +
                "rm\tremove errors in buffer from database" +
                "rst\treset buffer to entire database\n");
    }

    public static String promptCommand()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("[cmd]: ");
        return sc.next();
    }

    public static void displayList(ArrayList<Error> list)
    {
        for (int i = 0; i < list.size(); i ++)
        {
            System.out.println("(" + (i + 1) + ")\t" + list.get(i).getTitle() + "\t(" + list.get(i).tags + ")");
        }
    }

    public static ArrayList<Error> filterOr(ArrayList<Error> list, ArrayList<String> tagList)
    {
        System.out.println("[Filtering OR]");

        // remove all error objects that do not match any of the tags
        for (Error error: list)
        {
            boolean matchesNoTags = true;

            // iterate through tags
            for (String tag: tagList)
            {
                if (error.tags.contains(tag))
                {
                    matchesNoTags = false;
                    break;
                }
            }

            // remove object if it does not match any of the tags
            if (matchesNoTags)
            {
                list.remove(error);
            }
        }

        return list;
    }

    public static ArrayList<Error> filterAnd(ArrayList<Error> list, ArrayList<String> tagList)
    {
        System.out.println("[Filtering AND]");

        // remove all error objects that do not match all of the tags
        for (Error error: list)
        {
            for (String tag: tagList)
            {
                if (!error.tags.contains(tag))
                {
                    list.remove(error);
                    break;
                }
            }
        }

        return list;
    }

    // returns empty list if no args provided, handling the edge case of filtering by no tags
    public static ArrayList<String> pacmanArgs()
    {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> argsList = new ArrayList<String>();

        while (sc.hasNext())
        {
            argsList.add(sc.next());
        }

        return argsList;
    }

    public static String getNext()
    {
        Scanner sc = new Scanner(System.in);

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

        // add duplicates once
        for (Error errorA: listA)
        {
            for (Error errorB: listB)
            {
                if (errorA.equals(errorB))
                {
                    union.add(errorA);
                    listA.remove(errorA);
                    listB.remove(errorB);
                }
            }
        }
        
        // add unique elements
        for (Error error: listA)
        {
            union.add(error);
        }
        for (Error error: listB)
        {
            union.add(error);
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
                    listA.remove(errorA);
                    listB.remove(errorB);
                }
            }
        }

        return intersection;
    }

    public static ArrayList<Error> applyMultiplier(ArrayList<Error> list, double multiplier, ArrayList<String> tagList)
    {
        // if any of the tags in tagList are present, update the multiplier
        for (Error error: list)
        {
            for (String tag: tagList)
            {
                if (error.tags.contains(tag))
                {
                    error.setUrgencyMultiplier(multiplier);
                    break;
                }
            }
        }
        return list;
    }

    // recursive function for taking in a bounded index or :q
    public static String validateNum(int max, String message)
    {
        System.out.print(message);
        String choice = getNext();

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
                choice = validateNum(max, message);
            }

            index = Integer.parseInt(choice);

            if (index > max)
            {
                System.out.println("Exception: Must not exceed " + max + ".");
                choice = validateNum(max, message);
            }
        }

        return choice;
    }

    // relies on assumption that throwing an exception exits the function
    public static Review reviewError(Error error) throws QuitException
    {
        Scanner sc = new Scanner(System.in);
        Review review = new Review();

        System.out.println("[Review started]");
        System.out.println("[Title]: " + error.getTitle());
        System.out.println("[Pointer]: " + error.getPointer());

        System.out.print("[Press enter to continue]: ");
        if (sc.next().equals(":q"))
        {
            throw new QuitException();
        }

        System.out.println("[Error]: " + error.getDescription());
        if (sc.next().equals(":q"))
        {
            throw new QuitException();
        }
        else
        {
            String input = validateNum(100, "[Enter confidence: ]");
            if (input.equals(":q"))
            {
                throw new QuitException();
            }
            else
            {
                review.setConfidence(Integer.parseInt(input));
            }
        }

        return review;
    }

    public static ArrayList<Error> reviewQueue(ArrayList<Error> queue)
    {
        System.out.println("\n[Queue]");
        displayList(queue);

        String choice = validateNum(queue.size(), "[Enter no. of item to review]: ");

        while (!choice.equals(":q"))
        {
            int index = Integer.parseInt(choice) - 1;

            try
            {
                queue.get(index).addReview(reviewError(queue.get(index)));
            }
            catch (QuitException e)
            {
                System.out.println("[Review cancelled]");
            }

            choice = validateNum(queue.size(), "[Enter no. of item to review]: ");
        }

        return queue;
    }

    public static Error addError() throws QuitException
    {
        Scanner sc = new Scanner(System.in);

        // get title
        System.out.print("[Enter title]: ");
        sc.nextLine();  // clear buffer
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

        // get estimatedDuration
        String estimatedDurationString = validateNum(600, "[Enter estimated duration/mins]: ");

        if (estimatedDurationString.equals(":q"))
        {
            throw new QuitException();
        }

        int estimatedDuration = Integer.parseInt(estimatedDurationString);


        // get tags
        System.out.print("[Enter tags]: ");
        String tags = sc.nextLine();

        if (tags.equals(":q"))
        {
            throw new QuitException();
        }

        // construct and return new Error object
        return new Error(title, description, pointer, tags, estimatedDuration);
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

    public static void quit()
    {
        System.out.println("AlgorithmusIterationis exited.");
    }

    public static void main()
    {
        ArrayList<Error> database = new ArrayList<>();  // construct using json file parser
        ArrayList<Error> queue = new ArrayList<>();
        ArrayList<Error> buffer = database;

        // display welcome screen
        displayBanner();
        displayCommands();


        // main command loop
        String command;

        do
        {
            command = promptCommand();

            // check for database commands
            if (command.equals(":r"))
            {
                queue = reviewQueue(queue);
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
                }
                catch (QuitException e)
                {
                    System.out.println("[Add error operation cancelled]");
                }
            }
            else if (command.equals(":ddb"))
            {
                displayList(database);
            }
            // check for queue-buffer commands
            else if (command.equals("dq"))
            {
                displayList(queue);
            }
            else if (command.equals("db"))
            {
                displayList(buffer);
            }
            else if (command.equals("fn"))
            {
                ArrayList<String> tagList = pacmanArgs();
                buffer = filterAnd(buffer, tagList);
            }
            else if (command.equals("fu"))
            {
                ArrayList<String> tagList = pacmanArgs();
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
                queue = buffer;
            }
            else if (command.equals("m"))
            {
                double multiplier = 1.0;
                boolean isValidMultiplier = true;

                try
                {
                    multiplier = Double.parseDouble(getNext());
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
                    ArrayList<String> tagList = pacmanArgs();
                    buffer = applyMultiplier(buffer, multiplier, tagList);
                }

            }
            else if (command.equals("gf"))
            {
                int minutesFree = Integer.parseInt(validateNum(1440, "[Enter review duration/mins]: "));

                buffer = greedyFill(buffer, minutesFree);
            }
            else if (command.equals("rst"))
            {
                buffer = database;
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

        // quit the application
        quit();
    }
}