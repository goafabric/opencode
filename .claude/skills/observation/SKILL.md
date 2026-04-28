---
name: observation
description: Interact with the Observation MCP tools to retrieve patient laboratory data and vital signs by patient name. Use this skill whenever you need to look up lab results or vital signs for a patient.
---

# Observation MCP Skill

Use the Observation MCP tools to retrieve clinical patient data such as laboratory results and vital signs.

## Tool reference

| Tool | Description | Required parameter |
|------|-------------|-------------------|
| `observation_getLaboratoryDataByPatientName` | Retrieve laboratory data for a patient | `patientName` (string) |
| `observation_getVitalSignsByPatientName` | Retrieve vital signs for a patient | `patientName` (string) |

## Standard workflow

1. **Identify the patient** — confirm the patient's full name from the user's request.
2. **Call the appropriate tool** — use `observation_getLaboratoryDataByPatientName` for lab results or `observation_getVitalSignsByPatientName` for vital signs. Pass the patient name as `patientName`.
3. **Surface the result** — present the returned data clearly to the user.

## Notes

- Both tools accept a single required `patientName` parameter of type string.
- You may call both tools in parallel if the user needs both lab data and vital signs for the same patient.
