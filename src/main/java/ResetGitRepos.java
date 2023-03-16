import edu.lu.uni.serval.git.exception.GitRepositoryNotFoundException;
import edu.lu.uni.serval.git.exception.NotValidGitRepositoryException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResetGitRepos {
    public static void main(String [] args) throws GitAPIException, IOException, NotValidGitRepositoryException, GitRepositoryNotFoundException {

        /**
         Run Eclipse to suggest variable names of extracted expressions.
         **/
//        String projectName = "cassandra";
//        String basePath = "E:/VariableNameGeneration/BaseLine/BaseLineExperiment/";
//        String outputPath = basePath + "EvaluationResults/";
//        String specificVariableDeclarationFile = "E:\\VariableNameGeneration\\ExtractVariableData\\TestingData_cassandra_1287.txt";
////        String specificVariableDeclarationFile = "E:/VariableNameGeneration/ExtractVariableData/TestingData_cassandra_2132.txt";
//        try {
//            int cnt = 0;
//            ArrayList<String> variableLines = FileHelper.readFileByLines(specificVariableDeclarationFile);
//            System.out.println(variableLines.size());
//            for(String variableLine:variableLines){
//                System.out.println(cnt);
//                // resolve the value bag
//                String [] splitArray = variableLine.split("###");
//                String commitID = splitArray[3];
//                String nameAndInitializer = splitArray[0];
//                String variableName = nameAndInitializer.split("$$$")[0];
//                String javaFilePath = splitArray[1];
//                String methodInfo = splitArray[2];
//                String extractVariableInfos = methodInfo + "###" + variableName;
//                // checkout to specific commit ID
//                String gitPath = basePath+"TestProject/"+projectName+"/.git";
//                System.out.println(gitPath);
//                ResetGitRepos.rollReposToSpecificCommit(gitPath, commitID);
//                // parse and generate
//                cnt++;
//            }
//
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (NotValidGitRepositoryException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (GitRepositoryNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (GitAPIException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//	    try {
//	        eclipse_parseVariableNamesByProject_fragment(javaFilePath,outputPath,eclipse_variables,eclipse_generated_names);
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
    }

    public static void rollReposToSpecificCommit(String gitRoot, String commitID,List<String> commitIDs, List<RevCommit>commits) throws NotValidGitRepositoryException, GitRepositoryNotFoundException, IOException, GitAPIException {

        Boolean isRollBack;
        int index = commitIDs.indexOf(commitID);
        System.out.println(index);
        if(index!=-1){
            RevCommit commit = commits.get(index);
            System.out.println("commit.getId():"+commit.getId());
            RevCommit beforeCommit = commit.getParent(0);
            System.out.println("beforeCommit.getId():"+beforeCommit.getId());
            isRollBack = rollBackPreRevision(gitRoot, beforeCommit.getId());
            System.out.println(isRollBack);
        }
    }

    public static String rollReposToSpecificCommitAndGetID(String gitRoot, String commitID,List<String> commitIDs, List<RevCommit>commits) throws NotValidGitRepositoryException, GitRepositoryNotFoundException, IOException, GitAPIException {

        Boolean isRollBack;
        int index = commitIDs.indexOf(commitID);
        RevCommit parentCommit = null;
        System.out.println(index);
        if(index!=-1){
            RevCommit commit = commits.get(index);
            System.out.println("commit.getId():"+commit.getId());
            parentCommit = commit.getParent(0);
            System.out.println("parent commit.getId():"+parentCommit.getId());
            isRollBack = rollBackPreRevision(gitRoot, parentCommit.getId());
            System.out.println(isRollBack);
        }
        if(parentCommit!=null)
            return parentCommit.getName();
        else
            return "";

    }

//    public void rollAllReposToSpecificCommit(Map<String,Integer> reposAndLongestDuration) throws NotValidGitRepositoryException, GitRepositoryNotFoundException, IOException, GitAPIException {
//        List<String> projectList = readList(Configuration4InCon.JAVA_REPO_NAMES_FILE);
//        List<String> errorRepoList = new ArrayList<>();
//        for(String project:projectList){
//            int longestDuration = reposAndLongestDuration.get(project);
//            String gitRoot = Configuration4InCon.JAVA_REPOS_PATH + project + "/.git";
//            GitRepository gitRepository = new GitRepository(gitRoot);
//            gitRepository.open();
//
//            try{
//                List<RevCommit> commits = gitRepository.getAllCommits(false);
//                Boolean isRollBack;
//                isRollBack = rollBackPreRevision(gitRoot, commits.get(longestDuration).getId());
//                System.out.println(longestDuration);
//                System.out.println(isRollBack);
//
//            }
//            catch(org.eclipse.jgit.api.errors.CheckoutConflictException e){
//                e.printStackTrace();
//                errorRepoList.add(project);
//                continue;
//            }
//            catch(org.eclipse.jgit.api.errors.NoHeadException e1){
//                e1.printStackTrace();
//                errorRepoList.add(project);
//                continue;
//            }
//            catch(org.eclipse.jgit.api.errors.JGitInternalException e2){
//                System.err.println(project);
//                e2.printStackTrace();
//                errorRepoList.add(project);
//                continue;
//            }
//        }
//        System.out.println(errorRepoList);
//    }
//
//    public void rollAllReposToSpecificCommit(int initialOrLatest) throws NotValidGitRepositoryException, GitRepositoryNotFoundException, IOException, GitAPIException {
//        List<String> projectList = readList(Configuration4InCon.JAVA_REPO_NAMES_FILE);
//        List<String> errorRepoList = new ArrayList<>();
//        for(String project:projectList){
//            String gitRoot = Configuration4InCon.JAVA_REPOS_PATH + project + "/.git";
//            GitRepository gitRepository = new GitRepository(gitRoot);
//            gitRepository.open();
//
//            try{
//                List<RevCommit> commits = gitRepository.getAllCommits(false);
//                Boolean isRollBack;
//                if(initialOrLatest==0){
//                    isRollBack = rollBackPreRevision(gitRoot, commits.get(0).getId());
//                }
//                else{
//                    isRollBack = rollBackPreRevision(gitRoot, commits.get(commits.size() - 1).getId());
//                }
//                System.out.println(isRollBack);
//
//
//            }
//            catch(org.eclipse.jgit.api.errors.CheckoutConflictException e){
//                e.printStackTrace();
//                errorRepoList.add(project);
//                continue;
//            }
//            catch(org.eclipse.jgit.api.errors.NoHeadException e1){
//                e1.printStackTrace();
//                errorRepoList.add(project);
//                continue;
//            }
//        }
//        System.out.println(errorRepoList);
//    }
//
//
//    public void rollRepoToSpecificCommit(){
//        //        String gitRoot = "/home1/michael/BadMethodName/ctakes/.git";
////        String gitRoot = "/home1/michael/BadMethodName/JavaRepos/ofbiz/.git";
////        GitRepository gitRepository = new GitRepository(gitRoot);
////        gitRepository.open();
////        List<RevCommit> commits = gitRepository.getAllCommits(false);
////        System.out.println(commits.get(0).getId());
////        System.out.println(commits.get(1).getId());
////        System.out.println(commits.get(2).getId());
////        System.out.println(commits.get(commits.size()-1).getId());
////        System.out.println(commits.get(commits.size()-2).getId());
////        Boolean isRollBack = rollBackPreRevision(gitRoot,commits.get(0).getId());
////        System.out.println(isRollBack);
////        int num = resetGitRepos.getEachRepoJavaFilesNumber("/home1/michael/BadMethodName/JavaRepos/ofbiz");
////        System.out.println(num);
//    }
//    public void getReposJavaFilesNumber(){
//        ArrayList<Integer> numList = new ArrayList<>();
//        List<String> projectList = readList(Configuration4InCon.JAVA_REPO_NAMES_FILE);
//        HashMap<String,Integer> reposAndJavaFileNumber = new HashMap<>();
//
//        for(String project:projectList){
//            String projectPath = Configuration4InCon.JAVA_REPOS_PATH + project;
////            System.out.println(projectPath);
//            numList.add(getEachRepoJavaFilesNumber(projectPath));
//            reposAndJavaFileNumber.put(project,getEachRepoJavaFilesNumber(projectPath));
//        }
////        System.out.println(numList);
//        int cnt = 0;
//        for(int i:numList){
//            if(i==0){
//                cnt++;
//            }
//        }
//        System.out.println(numList.size());
//        System.out.println(cnt);
//        Map<String,Integer> reposAndJavaFileNumber1 = new MapSorter().sortByValueAscending(reposAndJavaFileNumber);
////        for(Map.Entry<String,Integer> entry:reposAndJavaFileNumber1.entrySet()){
////            System.out.println(entry.getKey()+":"+entry.getValue());
////        }
//        List<String> inValidReposList = new ArrayList<>();
//        for(Map.Entry<String,Integer> entry:reposAndJavaFileNumber1.entrySet()){
//            if(entry.getValue()==0){
//                inValidReposList.add(entry.getKey());
//            }
//        }
//        //serialized object
////        try {
//////            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("success1000MethodInfoAndBodyMap.txt"));
//////            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("methodInfoAndBodyMap.txt"));
//////            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("successMethodInfoAndBodyMap.txt"));
//////            ObjectOutputStream oos3 = new ObjectOutputStream(new FileOutputStream("successGetterMethodInfoAndBodyMap.txt"));
////            ObjectOutputStream oos4 = new ObjectOutputStream(new FileOutputStream("/tmp/inValidReposList.txt"));
//////            oos.writeObject(success1000MethodInfoAndBodyMap);
//////            oos1.writeObject(methodInfoAndBodyMap);
//////            oos2.writeObject(successMethodInfoAndBodyMap);
//////            oos3.writeObject(successGetterMethodInfoAndBodyMap);
////            oos4.writeObject(inValidReposList);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//
//
//    }
//    public void printReposJavaFilesNumber(){
//        List<String> projectList = readList(Configuration4InCon.JAVA_REPO_NAMES_FILE);
//        for(String project:projectList){
//            String projectPath = Configuration4InCon.JAVA_REPOS_PATH + project;
//            printEachRepoJavaFilesNumber(projectPath);
//        }
//    }
//    public static List<String> readList(String fileName) {
//        List<String> list = new ArrayList<>();
//        String content = FileHelper.readFile(fileName);
//        BufferedReader reader = new BufferedReader(new StringReader(content));
//        try {
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                list.add(line);
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//    private void printEachRepoJavaFilesNumber(String filePath){
//        List<File> list = FileHelper.getAllFiles(filePath,"java");
//        System.out.println(list.size());
//    }
//    private int getEachRepoJavaFilesNumber(String filePath){
//        List<File> list = FileHelper.getAllFiles(filePath,"java");
////        System.out.println(list.size());
//        return list.size();
//    }
//    public static boolean rollBackPreRevision(String gitRoot, String revision) throws IOException, GitAPIException {
//
//        Git git = Git.open(new File(gitRoot));
//
//        Repository repository = git.getRepository();
//
//        RevWalk walk = new RevWalk(repository);
//        ObjectId objId = repository.resolve(revision);
//        RevCommit revCommit = walk.parseCommit(objId);
////        String preVision = revCommit.getParent(0).getName();
//        String thisVision = revCommit.getName();
//        git.reset().setMode(ResetCommand.ResetType.HARD).setRef(thisVision).call();
//        repository.close();
//        return true;
//    }

    public static boolean rollBackPreRevision(String gitRoot, ObjectId revision) throws IOException, GitAPIException {

        Git git = Git.open(new File(gitRoot));

        Repository repository = git.getRepository();

        RevWalk walk = new RevWalk(repository);
        RevCommit revCommit = walk.parseCommit(revision);
//        String preVision = revCommit.getParent(0).getName();
        String thisVision = revCommit.getName();
        git.reset().setMode(ResetCommand.ResetType.HARD).setRef(thisVision).call();
        repository.close();
        return true;
    }
}
