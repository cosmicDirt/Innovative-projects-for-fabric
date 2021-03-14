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
	Name       string `json:"name"`
	StuNumber  string `json:"stuNumber"`
	Password   string `json:"password"`
	Gender     string `json:"gender"`
	University string `json:"university"`
	Major      string `json:"major"`
	Phone      string `json:"phone"`
	Email      string `json:"email"`
}

type Teacher struct {
	Name       string `json:"name"`
	TeaNumber  string `json:"teaNumber"`
	Password   string `json:"password"`
	Gender     string `json:"gender"`
	University string `json:"university"`
	Major      string `json:"major"`
	Phone      string `json:"phone"`
	Email      string `json:"email"`
}

type Comment struct {
	UserName   string `json:"userName"`
	UserAvatar string `json:"userAvatar"`
	Content    string `json:"content"`
	Time       string `json:"time"`
	Score      string `json:"score"`
}

type Appendix struct {
	FileName    string `json:"fileName"`
	Path        string `json:"path"`
	Description string `json:"description"`
	UpUser      string `json:"upUser"`
	UpTime      string `json:"upTime"`
}

type Subproject struct {
	SubproID        string     `json:"subproID"`
	SubproName      string     `json:"subproName"`
	ProID           string     `json:"proID"`
	SubproStartTime string     `json:"subproStartTime"`
	SubproEndTime   string     `json:"subproEndTime"`
	Difficulty      string     `json:"difficulty"`
	Info            string     `json:"info"`
	Member          []string   `json:"member"`
	Comment         []Comment  `json:"comment"`
	Appendix        []Appendix `json:"appendix"`
}


type Project struct{
	ProjectID string `json:"project_id"`
	ProjectName string `json:"project_name"`
	ProInfo string `json:"pro_info"`
	ProStartTime string `json:"pro_start_time"`
	ProEndTime string `json:"pro_end_time"`
	ProLeaderName string `json:"pro_leader_name"`
	ProTeacherName string `json:"pro_teacher_name"`
}

type StuInPro struct {
	SipID         string `json:"sip_id"`
	SipProID      string `json:"sip_pro_id"`
	SipStuName    string `json:"sip_stu_name"`
	RelativeScore string `json:"relative_score"`
	FinalScore    string `json:"final_score"`
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
	} else if function == "createProject" {
		return s.createProject(APIstub, args)
	} else if function == "deleteProject" {
		return s.deleteProject(APIstub, args)
	} else if function == "quitProject" {
		return s.quitProject(APIstub, args)
	} else if function == "AddProMem" {
		return s.AddProMem(APIstub, args)
	} else if function == "joinSubproject" {
		return s.joinSubproject(APIstub, args)
	} else if function == "quitSubproject" {
		return s.quitSubproject(APIstub, args)
	} else if function == "addComment" {
		return s.addComment(APIstub, args)
	} else if function == "uploadAppendixForSub" {
		return s.uploadAppendixForSub(APIstub, args)
	} else if function == "queryAppendix" {
		return s.queryAppendix(APIstub, args)
	} else if function == "queryAllAppendixForSub" {
		return s.queryAllAppendixForSub(APIstub, args)
	} else if function == "fixStuInfo" {
		return s.fixStu(APIstub, args)
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

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	var stu = Student{Name: args[0], StuNumber: args[1], Password: args[2], Gender: args[3], University: args[4], Major: args[5]}

	stuAsBytes, _ := json.Marshal(stu)
	APIstub.PutState(args[0], stuAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) createTeacher(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	var tea = Teacher{Name: args[0], TeaNumber: args[1], Password: args[2], Gender: args[3], University: args[4], Major: args[5]}

	teaAsBytes, _ := json.Marshal(tea)
	APIstub.PutState(args[0], teaAsBytes)

	return shim.Success(nil)
}

func (s *SmartContract) createSubproject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	var subpro Subproject
	if len(args) < 7 {
		return shim.Error("Incorrect number of arguments. Expecting 6+")
	} else if len(args) == 7 {
		subpro = Subproject{SubproID: args[0], ProID: args[1], SubproStartTime: args[2], SubproEndTime: args[3], Difficulty: args[4], Info: args[5],SubproName:args[6]}
	} else {
		var str []string
		for i := 7; i < len(args); i++ {
			str = append(str, args[i])
		}
		subpro = Subproject{SubproID: args[0], ProID: args[1], SubproStartTime: args[2], SubproEndTime: args[3], Difficulty: args[4], Info: args[5], SubproName:args[6],Member: str}
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

func (s *SmartContract) addComment(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}
	var comment = Comment{UserName: args[1], UserAvatar: args[2], Content: args[3], Time: args[4], Score: args[5]}
	subproAsBytes1, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes1, &subpro)
	subpro.Comment = append(subpro.Comment, comment)

	subproAsBytes2, _ := json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes2)

	return shim.Success(nil)
}

func (s *SmartContract) createProject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	var pro Project
	if len(args) != 8 {
		return shim.Error("Incorrect number of arguments. Expecting 4+")
	} else {
		pro = Project{ProjectID: args[1],ProInfo: args[2],ProLeaderName:args[3],ProTeacherName:args[4],ProStartTime:args[5],ProEndTime:args[6],ProjectName:args[7]}

		proAsBytes, _ := json.Marshal(pro)
		APIstub.PutState(args[0], proAsBytes)

		return shim.Success(nil)
	}
}

func (s *SmartContract) deleteProject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	APIstub.DelState(args[0])
	return shim.Success(nil)
}

func (s *SmartContract) quitProject(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	APIstub.DelState(args[0])
	return shim.Success(nil)
}

func (s *SmartContract) AddProMem(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	var sip StuInPro
	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	} else {
		sip = StuInPro{SipID: args[1], SipProID: args[2], SipStuName: args[3], FinalScore: args[5], RelativeScore: args[4]}
		sipAsBytes, _ := json.Marshal(sip)
		APIstub.PutState(args[0], sipAsBytes)

		return shim.Success(nil)
	}
}

func (s *SmartContract) uploadAppendixForSub(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}
	var appendix = Appendix{FileName: args[1], Path: args[2], Description: args[3], UpUser: args[4], UpTime: args[5]}
	subproAsBytes1, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes1, &subpro)
	subpro.Appendix = append(subpro.Appendix, appendix)

	subproAsBytes2, _ := json.Marshal(subpro)
	APIstub.PutState(args[0], subproAsBytes2)

	return shim.Success(nil)
}

func (s *SmartContract) queryAppendix(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting 4")
	}
	var appendix = []Appendix{}
	subproAsBytes1, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes1, &subpro)
	appendix = subpro.Appendix

	for i := 0; i < len(appendix); i++ {
		if appendix[i].FileName == args[1] && appendix[i].UpUser == args[2] && appendix[i].UpTime == args[3] {
			appendixAsBytes, _ := json.Marshal(appendix[i])
			return shim.Success(appendixAsBytes)
		}
	}
	return shim.Error("Not found the appendix")
}

func (s *SmartContract) queryAllAppendixForSub(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	var appendix = []Appendix{}
	subproAsBytes1, _ := APIstub.GetState(args[0])
	subpro := Subproject{}

	json.Unmarshal(subproAsBytes1, &subpro)
	appendix = subpro.Appendix

	appendixAsBytes, _ := json.Marshal(appendix)
	return shim.Success(appendixAsBytes)

}

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

func (s *SmartContract) fixStu(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 9 {
		return shim.Error("Incorrect number of arguments. Expecting 6")
	}

	var stu = Student{Name: args[1], StuNumber: args[2], Password: args[3], Gender: args[4], University: args[5], Major: args[6], Phone: args[7], Email: args[8]}

	stuAsBytes, _ := json.Marshal(stu)
	APIstub.PutState(args[0], stuAsBytes)

	return shim.Success(nil)
}

func main() {

	// Create a new Smart Contract
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}
}
