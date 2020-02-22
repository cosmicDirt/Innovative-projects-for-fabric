/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * The sample smart contract for documentation topic:
 * Writing Your First Blockchain Application
 */

package main

/* Imports
 * 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the car structure, with 4 properties.  Structure tags are used by encoding/json library
type Student struct {
	Name      string `json:"name"`
	StuNumber string `json:"stuNumber"`
	Password  string `json:"password"`
	Gender    string `json:"gender"`
	Phone     string `json:"phone"`
	Email     string `json:"email"`
}

type Teacher struct {
	Name      string `json:"name"`
	TeaNumber string `json:"teaNumber"`
	Password  string `json:"password"`
	Gender    string `json:"gender"`
	Phone     string `json:"phone"`
	Email     string `json:"email"`
}

type Comment struct {
	UserName   string `json:"userName"`
	UserAvatar string `json:"userAvatar"`
	Content    string `json:"content"`
	Time       string `json:"time"`
}

type Subproject struct {
	SubproID   string    `json:"subproID"`
	ProID      string    `json:"proID"`
	SubproTime string    `json:"subproTime"`
	Info       string    `json:"info"`
	Member     []string  `json:"member"`
	Comment    []Comment `json:"comment"`
}

/*
 * The Init method is called when the Smart Contract "fabcar" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "fabcar"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 */
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Smart Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "queryStudentByName" {
		return s.queryStudentByName(APIstub, args)
	} else if function == "queryTeacherByName" {
		return s.queryTeacherByName(APIstub, args)
	} else if function == "queryByKey" {
		return s.queryByKey(APIstub, args)
	} else if function == "queryHistoryByKey" {
		return s.queryHistoryByKey(APIstub, args)
	} else if function == "getQueryResultForQueryString" {
		return s.getQueryResultForQueryString(APIstub, args)
	} else if function == "initLedger" {
		return s.initLedger(APIstub)
	} else if function == "createStudent" {
		return s.createStudent(APIstub, args)
	} else if function == "createTeacher" {
		return s.createTeacher(APIstub, args)
	} else if function == "createSubproject" {
		return s.createSubproject(APIstub, args)
	} else if function == "deleteSubproject" {
		return s.deleteSubproject(APIstub, args)
	} else if function == "joinSubproject" {
		return s.joinSubproject(APIstub, args)
	} else if function == "quitSubproject" {
		return s.quitSubproject(APIstub, args)
	} else if function == "AddAComment" {
		return s.AddAComment(APIstub, args)
	}

	return shim.Error("Invalid Smart Contract function name.")
}

func (s *SmartContract) queryStudentByName(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	studentAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(studentAsBytes)
}

func (s *SmartContract) queryTeacherByName(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	teacherAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(teacherAsBytes)
}

func (s *SmartContract) queryByKey(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	valueAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(valueAsBytes)
}

func (s *SmartContract) queryHistoryByKey(stub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	resultsIterator, err := stub.GetHistoryForKey(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing historic values for the marble
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		response, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"TxId\":")
		buffer.WriteString("\"")
		buffer.WriteString(response.TxId)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Value\":")
		// if it was a delete operation on given key, then we need to set the
		//corresponding value null. Else, we will write the response.Value
		//as-is (as the Value itself a JSON marble)
		if response.IsDelete {
			buffer.WriteString("null")
		} else {
			buffer.WriteString(string(response.Value))
		}

		buffer.WriteString(", \"Timestamp\":")
		buffer.WriteString("\"")
		buffer.WriteString(time.Unix(response.Timestamp.Seconds, int64(response.Timestamp.Nanos)).String())
		buffer.WriteString("\"")

		buffer.WriteString(", \"IsDelete\":")
		buffer.WriteString("\"")
		buffer.WriteString(strconv.FormatBool(response.IsDelete))
		buffer.WriteString("\"")

		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- getHistoryForMarble returning:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	stus := []Student{
		{Name: "stu1", Password: "pss1"},
		{Name: "stu2", Password: "pss2"},
	}

	i := 0
	for i < len(stus) {
		fmt.Println("i is ", i)
		stuAsBytes, _ := json.Marshal(stus[i])
		APIstub.PutState(stus[i].Name, stuAsBytes)
		fmt.Println("Added", stus[i])
		i = i + 1
	}

	return shim.Success(nil)
}

func (s *SmartContract) createStudent(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	var stu = Student{Name: args[0], StuNumber: args[1], Password: args[2]}

	stuAsBytes, _ := json.Marshal(stu)
	APIstub.PutState(args[0], stuAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) createTeacher(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 3")
	}

	var tea = Teacher{Name: args[0], TeaNumber: args[1], Password: args[2]}

	teaAsBytes, _ := json.Marshal(tea)
	APIstub.PutState(args[0], teaAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) createSubproject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	var subpro Subproject
	if len(args) < 4 {
		return shim.Error("Incorrect number of arguments. Expecting 5+")
	} else if len(args) == 4 {
		subpro = Subproject{SubproID: args[0], ProID: args[1], SubproTime: args[2], Info: args[3]}
	} else {
		var str []string
		for i := 4; i < len(args); i++ {
			str = append(str, args[i])
		}
		subpro = Subproject{SubproID: args[0], ProID: args[1], SubproTime: args[2], Info: args[3], Member: str}
	}
	subproAsBytes, _ := json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) deleteSubproject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	APIstub.DelState(args[0])

	return shim.Success(nil)
}

func (s *SmartContract) joinSubproject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	subproAsBytes, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes, &subpro)
	subpro.Member = append(subpro.Member, args[1])

	subproAsBytes, _ = json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) quitSubproject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	subproAsBytes, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	var index, flag int
	json.Unmarshal(subproAsBytes, &subpro)
	for i := 0; i < len(subpro.Member); i++ {
		if subpro.Member[i] == args[1] {
			index = i
			flag = 1
		}
	}
	if flag == 1 {
		subpro.Member = append(subpro.Member[:index], subpro.Member[index+1:]...)
	}

	subproAsBytes, _ = json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) AddAComment(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}
	var comment = Comment{UserName: args[1], UserAvatar: args[2], Content: args[3], Time: args[4]}
	subproAsBytes1, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes1, &subpro)
	subpro.Comment = append(subpro.Comment, comment)

	subproAsBytes2, _ := json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes2)

	return shim.Success(nil)
}

/*func (s *SmartContract) queryAllCars(APIstub shim.ChaincodeStubInterface) sc.Response {
	startKey := "CAR0"
	endKey := "CAR999"
	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()
	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")
		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
	fmt.Printf("- queryAllCars:\n%s\n", buffer.String())
	return shim.Success(buffer.Bytes())
}
func (s *SmartContract) changeCarOwner(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}
	carAsBytes, _ := APIstub.GetState(args[0])
	car := Car{}
	json.Unmarshal(carAsBytes, &car)
	car.Owner = args[1]
	carAsBytes, _ = json.Marshal(car)
	APIstub.PutState(args[0], carAsBytes)
	return shim.Success(nil)
}*/

// The main function is only relevant in unit test mode. Only included here for completeness.


func (s *SmartContract) getQueryResultForQueryString(stub shim.ChaincodeStubInterface, args []string) sc.Response {
	resultsIterator, err := stub.GetQueryResult(args[0])
	defer resultsIterator.Close()
	if err != nil {
		return shim.Error(err.Error())
	}
	// buffer is a JSON array containing QueryRecords
	var buffer bytes.Buffer
	buffer.WriteString("[")
    bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse,
			err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")
		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")
	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())
	return shim.Success(buffer.Bytes())
}

func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
