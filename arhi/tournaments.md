# Tournaments
This is a sample Tournaments Service based on the OpenAPI 3.0 specification.

## Version: 1.0.11

### Terms of service
http://swagger.io/terms/

**Contact information:**  
apiteam@swagger.io

**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[Find out more about Swagger](http://swagger.io)
### /tournament

#### POST
##### Summary:

Add a new tournament

##### Description:

Add a new tournament

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful operation |
| 400 | Invalid input |
| 422 | Validation exception |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| petstore_auth | write:tournaments | read:tournaments |

### /tournament/{tournamentId}

#### GET
##### Summary:

Find tournament by ID

##### Description:

Returns a single tournament

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| tournamentId | path | ID of tournament to return | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid ID supplied |
| 404 | Tournament not found |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| api_key | | |
| petstore_auth | write:tournaments | read:tournaments |

### /tournament/{tournamentId}/rating

#### GET
##### Summary:

Get tournament rating by ID

##### Description:

Returns a single rating

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| tournamentId | path | ID of tournament | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid ID supplied |
| 404 | Tournament not found |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| api_key | | |
| petstore_auth | write:tournaments | read:tournaments |

### /tournament/{tournamentId}/user/{participantId}

#### POST
##### Summary:

Add a partipant to tournament

##### Description:

Add a partipant to tournament

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| tournamentId | path | ID of battle to return | Yes | long |
| participantId | path | ID of battle to return | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful operation |
| 400 | Invalid input |
| 422 | Validation exception |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| petstore_auth | write:tournaments | read:tournaments |
