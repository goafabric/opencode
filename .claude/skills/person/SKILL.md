---
name: person
description: Interact with the Person MCP tools to find, retrieve, and save person records. Use this skill whenever you need to search for people, look up a person by ID, or create/update a person with optional address data.
---

# Person MCP Skill

Use the built-in Person MCP tools to manage person records.

## Tool reference

| Tool | Description | Key parameters |
|------|-------------|----------------|
| `person_find` | Search for people by name (paginated) | `personSearch` (`firstName`, `lastName`), `page`, `size` |
| `person_getById` | Retrieve a single person by their ID | `id` (string) |
| `person_save` | Create or update a person record | `person` object (see schema below) |

### Person object schema

```json
{
  "id": "string (omit for new records)",
  "version": "integer (required for updates, omit for new records)",
  "firstName": "string",
  "lastName": "string",
  "address": [
    {
      "id": "string (omit for new addresses)",
      "version": "integer (required for updates, omit for new)",
      "street": "string",
      "city": "string"
    }
  ]
}
```

## Standard workflows

### Search for a person
1. Call `person_find` with `personSearch` containing at least one of `firstName` or `lastName`.
2. Use `page: 1` and a reasonable `size` (e.g. `10`) to start.
3. Present the results to the user; paginate further if needed.

### Get a person by ID
1. Call `person_getById` with the person's `id`.
2. Return the full person record to the user.

### Create a new person
1. Call `person_save` with a `person` object that **omits** `id` and `version`.
2. Include `address` array if address data is provided; omit it otherwise.
3. Return the saved record (including the generated `id`) to the user.

### Update an existing person
1. First retrieve the current record via `person_find` or `person_getById` to obtain the current `version`.
2. Call `person_save` with the full `person` object including `id` and the current `version`.
3. For existing addresses include their `id` and `version`; for new addresses omit both.
4. Return the updated record to the user.

## Notes

- `version` is used for optimistic locking. Always read the latest record before saving to avoid version conflicts.
- Pagination is (`page: 1` is the first page).
- `address` is optional; a person can be saved without any addresses.
