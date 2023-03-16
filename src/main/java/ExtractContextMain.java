import edu.lu.uni.serval.git.travel.GitRepository;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import utils.FileHelper;
import edu.lu.uni.serval.git.exception.GitRepositoryNotFoundException;
import edu.lu.uni.serval.git.exception.NotValidGitRepositoryException;
import edu.lu.uni.serval.git.travel.GitRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class ExtractContextMain {
    public static void  main(String [] args) throws IOException, GitAPIException, NotValidGitRepositoryException, GitRepositoryNotFoundException {

        /*
        final version
         */
//        String projectNamesFile = "/home1/tyc/Top1kProjectName.txt";
//        ArrayList<String> projectNames = FileHelper.readFileByLines(projectNamesFile);
//        for(int i=0;i<projectNames.size();i++){
//            String projectName = projectNames.get(i);
//            extractContext(projectName);
//        }

        /*
        test version
         */
        extractContext("apache_cassandra");
    }
    public static void extractContext(String projectName) throws IOException, GitAPIException, NotValidGitRepositoryException, GitRepositoryNotFoundException {
        String basePath = "/home1/michael/ExtractVariableViaRM/output/records4Context/";
        String outputPath = "/home1/michael/ExtractVariableViaRM/output/context/";
        String recordPath = basePath + projectName + ".txt";
        ArrayList<String> variableLines = FileHelper.readFileByLines(recordPath);
        StringBuilder contexts = new StringBuilder();
        String gitRoot = basePath + projectName + "/.git";
        GitRepository gitRepository = openRepository(basePath,projectName,gitRoot);
        List<RevCommit> commits = gitRepository.getAllCommits(false);
        ArrayList<String> commitIDs = new ArrayList<>();
        for(RevCommit commit:commits){
            commitIDs.add(commit.getName());
        }
        int cnt = 0;
        for(String variableLine:variableLines){
            System.out.println(cnt);
            cnt++;
            System.out.println(variableLine);
            // resolve the value bag
            HashMap<String,String> valueMap = resolveValueBag(variableLine);
            assert valueMap != null;
            String commitID = valueMap.get("commitID");
            String initializer = valueMap.get("initializer");
            String involvedExpression = valueMap.get("involvedExpression");
            String variableName = valueMap.get("variableName");
            /*
            checkout to specific commit, and take the java file
             */

            String beforeCommitID = ResetGitRepos.rollReposToSpecificCommitAndGetID(gitRoot, commitID,commitIDs,commits);


            contexts.append(initializer).append("###").append(involvedExpression).append("###").append(variableName).append("\n");
            }

        // output to file
//        FileHelper.outputToFile(outputPath+projectName+".txt",contexts,false);

    }
    public static HashMap<String,String> resolveValueBag(String variableLine) {
//        f0e1b75fb9632a50cf37307630493b9780a26bb0###/addthis_stream-lib/src/test/java/com/clearspring/analytics/stream/cardinality/TestCountThenEstimate.java###/TestCountThenEstimate.java###com.clearspring.analytics.stream.cardinality.TestCountThenEstimate###assertCountThenEstimateEquals:CountThenEstimate CountThenEstimate ###assertArrayEquals(expected.estimator.getBytes(),actual.estimator.getBytes());###expBytes###expected.estimator.getBytes()###256:13:256:91
        // commitID+"###"+javaFilePath +"###" javaFileName +"###" + classPath + "###" + methodInfo +"###" + involvedExpression +"###" + variableName+"###" + initializer +"###" + offset;
        String[] splitArray = variableLine.replaceAll("\n", "").split("###");
        HashMap<String, String> values = new HashMap<>();
        if (splitArray.length != 9) {
            System.out.println("not valid record <8");
            return null;
        } else {
            String commitID = splitArray[0];
            values.put("commitID", commitID);
            String javaFileName = splitArray[2];
            values.put("javaFileName", javaFileName);
            String javaFilePath = splitArray[1];
            values.put("javaFilePath", javaFilePath);
            String classPath = splitArray[3];
            values.put("classPath", classPath);
            String methodInfo = splitArray[4];
            values.put("methodInfo", methodInfo);
            String involvedExpression = splitArray[5];
            values.put("involvedExpression", involvedExpression);
            String variableName = splitArray[6];
            values.put("variableName", variableName);
            String initializer = splitArray[7];
            values.put("initializer", initializer);
            String lineAndColumn = splitArray[8];
            values.put("lineAndColumn", lineAndColumn);
        }
        return values;
    }

    public static GitRepository openRepository(String basePath,String projectName,String gitRoot) throws NotValidGitRepositoryException, GitRepositoryNotFoundException, IOException {
        String revisedPath = basePath +"/" + projectName + File.separator + "Rev";
        String previousPath = basePath +"/" + projectName + File.separator + "Pre";
        edu.lu.uni.serval.utils.FileHelper.createDirectory(revisedPath);
        edu.lu.uni.serval.utils.FileHelper.createDirectory(previousPath);
        GitRepository gitRepository = new GitRepository(gitRoot,revisedPath,previousPath);
        gitRepository.open();
        return gitRepository;
    }
    public static int getOffset(String filePath, int startLine, int startColumn){
        String contents = FileHelper.readFile(filePath);
        String temp = "";
        String [] split = contents.split("\n");
        try{
            for(int i=0;i<startLine-1;i++){
                temp+=split[i]+"\n";
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("1");
            return -2;
        }
        return temp.length() + startColumn;
    }
}
