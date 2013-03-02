package com.example.gitupload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitConnection {

	private GitHubClient gitClient = null;

	private IssueService issueService = null;

	private RepositoryService repoService = null;

	private boolean authenticated = false;

	public GitConnection() {

	}

	public void createConnection(String userName, String password) {
		if (gitClient == null) {
			gitClient = new GitHubClient();
		}

		gitClient.setCredentials(userName, password);
		authenticated = true;

		issueService = new IssueService(gitClient);
		repoService = new RepositoryService();
	}

	public IssueService getIssueService() {
		return issueService;
	}

	public GitHubClient getGitHubClient() {
		return gitClient;
	}

	/*
	 * Generates notify event for issueService to update modified issues
	 */
	public void updateIssue() {
		synchronized (issueService) {
			issueService.notify();
		}
	}

	/*
	 * Returns a list of issues as strings.
	 * 
	 * @param repoName The repository name for which to return issues
	 * 
	 * @returns a list of issues as strings.
	 */
	public List<String> getIssueList(String repoName) throws IOException {
		if (!authenticated)
			return null;

		List<Issue> issues = issueService.getIssues(gitClient.getUser(),
				repoName, null);

		List<String> issueList = new ArrayList<String>();
		for (Issue issue : issues)
			issueList.add(issue.getTitle());

		return issueList;
	}

	/*
	 * Creates an issue
	 * 
	 * @param repoName Repository name to create the issue
	 * 
	 * @param issueTitle The title of the issue
	 * 
	 * @param issueBody Comment/Body of the issue
	 */
	public void createIssue(String repoName, String issueTitle, String issueBody)
			throws IOException {
		if (!authenticated)
			return;

		Repository repo = repoService.getRepository(gitClient.getUser(),
				repoName);

		Issue issue = new Issue();
		issue.setBodyText(issueBody);
		issue.setTitle(issueTitle);

		issueService.createIssue(gitClient.getUser(), repoName, issue);
	}

	/*
	 * Gets the names of the repositories for the user
	 * 
	 * @returns A list of String representing repository names
	 */
	public List<String> getRepositories() throws IOException {
		List<String> newList = new ArrayList<String>();
		for (Repository repo : repoService.getRepositories(gitClient.getUser())) {
			newList.add(repo.getName());
		}
		return newList;
	}

	/*
	 * Creates a comment to an existing issue
	 * 
	 * @param Repository name where the issue is found
	 * @param issueName Name of the existing issue to create a comment
	 * @param comment Comment text
	 * */
	public void createCommentToIssue(String repoName, String issueName, String comment) throws IOException {
    	if (!authenticated)
            return;

        Issue myIssue = null;
        List<Issue> issues = issueService.getIssues(gitClient.getUser(), repoName, null);
        for (Issue i : issues)
            if (issueName.compareTo(i.getTitle()) == 0)
            	myIssue = i;

        if (myIssue == null)
                return;

        issueService.createComment(gitClient.getUser(), repoName, myIssue.getNumber(), comment);
        synchronized (issueService) {
        	issueService.notify();
		}
	}
}
