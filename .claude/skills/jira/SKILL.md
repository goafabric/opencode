---
name: jira
description: Interact with the Atlassian Jira MCP
---

# Atlassian Jira Skill

## Connection

- **MCP server:** `atlassian` (already connected)
- **Jira access:** read-write
- **Known project:** `KAN`

Call `getAccessibleAtlassianResources` once per session if the cloudId is unknown; otherwise reuse the cached value above.

## Common Operations

### Search issues (JQL)
Use `searchJiraIssuesUsingJql` with a valid JQL query. A bare `ORDER BY` with no filter is rejected — always include at least one filter clause, e.g.:

```
project = KAN ORDER BY created DESC
project is not EMPTY ORDER BY created DESC
assignee = currentUser() AND status != Done
```

### Get a single issue
Use `getJiraIssue` with `issueIdOrKey` (e.g. `KAN-1`).

### Create an issue
Use `createJiraIssue`. Required: `cloudId`, `projectKey`, `summary`, `issueType`.
Common issue types: `Task`, `Story`, `Bug`, `Epic`, `Subtask`.
Pass custom fields via `additional_fields`.

### Edit an issue
Use `editJiraIssue`. Pass only the fields to change in `fields` or `additional_fields`.
To clear a field, pass explicit `null`.

### Transition (change status)
Use `transitionJiraIssue`. Provide `transitionName` (e.g. `"In Progress"`, `"Done"`) or `transitionId`.
To move to backlog: set `assignToBacklog: true`. To assign to sprint: set `sprintId`.

### Add / edit a comment
Use `addOrEditJiraIssueComment`. Omit `commentId` to add; provide it to edit.
Comment body supports markdown by default.

### Link issues
Use `addTeamworkGraphContext` with `relationshipType`:
- `jira-work-item-links-jira-work-item`
- `jira-work-item-blocks-jira-work-item`
- `jira-work-item-tracks-atlassian-project`
- `jira-work-item-contributes-to-atlassian-goal`

### Assign to sprint / backlog
Use `transitionJiraIssue` with `sprintId` (numeric) or `assignToBacklog: true`.
To find sprint IDs, use `discover` with query `"list sprints for board"`.

## Workflow Tips

- **JQL requires at least one filter** — never use bare `ORDER BY` without a `WHERE`-style clause.
- **Custom fields** go in `additional_fields`, keyed by human name or `customfield_*` ID.
- **Views:** use `view: "compact"` for scanning, `view: "evidence"` when custom fields (story points, sprint) are needed.
- **Pagination:** use `nextPageToken` to page through large result sets; stop when `isLast` is `true`.
- **Unknown operations:** call `discover` first with a verb phrase (e.g. `"list sprints for board"`).
