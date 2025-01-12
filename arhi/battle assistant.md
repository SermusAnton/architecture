# Battle Assistant
This is a sample Battle Assistant Service based on the OpenAPI 3.0 specification.

## Version: 1.0.11

### Terms of service
http://swagger.io/terms/

**Contact information:**  
apiteam@swagger.io

**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[Find out more about Swagger](http://swagger.io)
### /battle

#### POST
##### Summary:

Add a new battle

##### Description:

Add a new battle

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successful operation |
| 400 | Invalid input |
| 422 | Validation exception |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| petstore_auth | write:battles | read:battles |

#### GET
##### Summary:

Finds all battles

##### Description:

Returns a array battles

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid status value |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| petstore_auth | write:battles | read:battles |

### /battle/{battleId}

#### GET
##### Summary:

Find battle by ID

##### Description:

Returns a single battle

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| battleId | path | ID of battle to return | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid ID supplied |
| 404 | Battle not found |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| api_key | | |
| petstore_auth | write:battles | read:battles |

#### DELETE
##### Summary:

Deletes a battle

##### Description:

delete a battle

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| api_key | header |  | No | string |
| battleId | path | Battle id to delete | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 400 | Invalid battle value |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| petstore_auth | write:battles | read:battle |

### /battle/{battleId}/user/{participantId}

#### POST
##### Summary:

Invite a participant to the battle

##### Description:

Invite a participant to the battle

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| battleId | path | ID of battle to return | Yes | long |
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
| petstore_auth | write:battles | read:battles |

### /battle/{battleId}/result

#### GET
##### Summary:

Find result of battle by ID

##### Description:

Returns a single result

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| battleId | path | ID of battle | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid ID supplied |
| 404 | Result not found |

##### Security

| Security Schema | Scopes | |
| --- | --- | --- |
| api_key | | |
| petstore_auth | write:battles | read:battles |
