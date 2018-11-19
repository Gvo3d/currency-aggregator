


Create DB:
{project-dir}/scripts/create_database.sh
NOTE: to launch a working DB - docker is a must!
If you have a specific address for a database, past it into application.yml into the property of url.

Requests cases:

1)Get specific currency:
URL LIKE: http://localhost:8080/currency?type=USD&buying=true&ascend=false
METHOD: GET
params:
type - currency type (from NationalCurrencies names)
buying - boolean, if false - will be selling
ascend - sort by values(ascend or false - descend)

2)Upload files:
URL: http://localhost:8080/files
Must be send as a multipart/form-data with file in the parameter with name: "file".
METHOD: POST
Filetypes allowed: CSV, JSON, XML
File must have a name from bank, like: "Bank 1.json"
Data in the files must be interpreted as an array.
Examples are located in the "data" folder of the project.

3)Change operation allowance or delete any currency by bank:
LIKE URL: http://localhost:8080/currency?delete=false
METHOD: PUT
Content-Type: application/json
Body like: {"name":"GBP","action":"SELLING","bank":"Bank 1","allowed":true}
Comments: if name is not stated, then all currencies will be targeted, if action is not stated, then all types of action will be targeted, bank must be always present and allowed parameter can be not stated if delete parameter in url is true.
Delete parameter is for disabling(alike deletion) of the currencies.
If you do not want to state some parameters, just don't write them in the json, like: {"bank":"Bank 1","allowed":false} - in this case all currencies with all actions will be marked as non-operational for Bank 1.

4)Get best prices for all currencies:
URL: http://localhost:8080/files
METHOD: GET
It's better to load via the browser.

5)Change or upload a new data(one per time):
URL: http://localhost:8080/currency
METHOD: POST
DATA LIKE: 
{
"name": "USD",
"bank": "Bank 3",
"action": "BUYING",
"value": "18.05",
"allowed": true
}