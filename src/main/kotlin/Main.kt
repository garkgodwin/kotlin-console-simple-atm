import java.text.DecimalFormat
import java.text.NumberFormat

//DATA CLASS FOR CUSTOMER
data class Customer(
    var name: String,
    var accountNumber: String,
    var pin: String, //must be a 4 digit integer but saved as string
    var balance: Double,
)

/*
====================================================MOTHER FUNCTION====================================================
 */
//variables
fun main(args: Array<String>) {
    printIntro() //this prints the introduction of the simple atm
    /*
        MAIN VARIABLES
     */
    var systemStart: Boolean = true
    var authenticated:Boolean = false
    var pinIsValid: Boolean
    while(systemStart){
        printSystemStart()
        val pin = readLine().toString()
        pinIsValid = isNotEmpty(pin, "Please do not leave the Pin empty.")
        //CLOSE SYSTEM WHEN ACTION IS [E]
        if(pinIsValid){
            if(pin == "E" || pin == "e"){
                systemStart = false
            }
        }
        //AUTHENTICATE PIN TO ACCESS THE INNER SYSTEM
        if(pinIsValid && systemStart){
            pinIsValid = checkPinValidity(pin)
            if(pinIsValid){
                //authenticate
                var loggedInCustomer: Customer = getCustomerData(pin)
                authenticated =  loggedInCustomer.pin != "" && loggedInCustomer.accountNumber != ""
                if(authenticated){
                    val name = loggedInCustomer.name
                    printAuthenticated(name)
                }
                else{
                    println("You have entered the wrong pin!")
                }
            }
        }
        //MAIN FEATURES BLOCK
        while(pinIsValid && authenticated){
            printActions()
            var action = readLine().toString()
            var actionIsValid:Boolean = false;
            actionIsValid = isNotEmpty(action, "Please do not leave the action type empty.")
            //actions to be selected by corresponding letters
            when(action){
                "E" -> {
                    authenticated = false
                    systemStart = false
                }
                "e" -> {
                    authenticated = false
                    systemStart = false
                }
                "G" -> authenticated = false
                "g" -> authenticated = false
                "B" -> showBalance(pin)
                "b" -> showBalance(pin)
                "D" -> depositSystem(pin)
                "d" -> depositSystem(pin)
                "W" -> withdrawSystem(pin)
                "w" -> withdrawSystem(pin)
                "S" -> sendMoneySystem(pin)
                "s" -> sendMoneySystem(pin)
                else -> println("Please enter a valid action.")
            }
        }
    }
    printOutro() //this prints the outro of the simple atm
}

/*
====================================================MAIN FEATURES====================================================
 */
//===========SHOW BALANCE FEATURE===========
fun showBalance (pin: String){
    //prints the customer's balance by looping and checking pin is equal.
    for(customer in customerList){
        if(customer.pin == pin){
            var bal = customer.balance
            println("Your remaining balance is ₱${toCurrency(bal.toString())}")
        }
    }
}
//===========DEPOSIT FEATURE================
fun depositSystem (pin: String){
    var depositing: Boolean = true
    //main loop of depositingSystem
    while(depositing){
        var isValid: Boolean = false
        //inner loop to check validity of inputs
        while(!isValid){
            printMainFeatureTitle("DEPOSIT")
            print("> Deposit (should not be less than ₱100): ")
            val value = readLine().toString()
            isValid = isNotEmpty(value, "Please do not leave the Deposit Amount empty.")
            if(isValid){
                if(value == "C" || value == "c") {
                    isValid = true // to exit valid loop
                    depositing = false // to exit deposit loop
                }
            }
            //if still valid and still depositing
            if(isValid && depositing){
                isValid = isNumber(value, "Please enter a valid number for Deposit Amount.")
                if(isValid){
                    val money = value.toDouble()
                    if(money< 100.0){
                        println("Amount should be greater than ₱100.")
                    }
                    else{
                        for(customer in customerList){
                            if(customer.pin == pin){
                                val newValue = customer.balance+money
                                customerList[customerList.indexOf(customer)].balance = newValue
                                println("You deposited ₱${toCurrency(newValue.toString())}")
                                depositing = false
                            }
                        }
                    }
                }
            }
        }
    }
}
//===========WITHDRAW FEATURE===============
fun withdrawSystem (pin: String){
    var withdrawing: Boolean = true
    while(withdrawing){
        var isValid = false
        while(!isValid){
            printMainFeatureTitle("WITHDRAW")
            print("> Withdraw Amount: ")
            var value = readLine().toString()
            isValid = isNotEmpty(value, "Please do not leave the amount field empty.")
            if(isValid){
                if(value == "C" || value == "c"){
                    withdrawing = false
                    isValid = true
                }
            }
            if(isValid && withdrawing){
                isValid = isNumber(value, "Please make sure the amount is a number.")
                if(isValid){
                    isValid = !amountIsLessOrZero(value)
                }
                if(isValid) {
                    val money = value.toDouble()
                    for (customer in customerList) {
                        if (customer.pin == pin) {
                            if(money > customer.balance){
                                println("Cannot withdraw value greater than your balance.")
                                isValid = false
                            }
                            else{
                                val newValue = customer.balance - money
                                customerList[customerList.indexOf(customer)].balance = newValue
                                println("You have withdrawn ₱${toCurrency(money.toString())}")
                                withdrawing = false
                                isValid = true
                            }
                        }
                    }
                }

            }
        }
    }
}

//-variables for send money feature
var sending: Boolean = true //for sendMoneySystem function
var sendIsValid: Boolean = false //for confirmation
//===========SEND MONEY FEATURE=============
fun sendMoneySystem (pin: String){
    sending = true
    sendIsValid = false
    //sendMoneySystem main loop
    while(sending){
        //prints main feature title
        printMainFeatureTitle("SEND MONEY")
        //get customer logged in data and display
        for(customer in customerList){
            if(customer.pin == pin){
                println("Sender: ${customer.name} | Account Number: ${customer.accountNumber} " +
                        "| Balance: ₱${toCurrency(customer.balance.toString())}")
            }
        }
        var name:String = ""
        var accountNumber:String = ""
        var amount:String = ""
        // this checks validity of recipient's name and saves to "name" if valid
        if(sending){
            var valueIsValid:Boolean = false
            while(!valueIsValid) {
                print("> Recipient Name: ")
                name = readLine().toString()
                valueIsValid = isNotEmpty(name, "Please do not leave recipient's name empty.")
                if(valueIsValid){
                    if (name == "C" || name == "c") {
                        sending = false
                    }
                }
            }
        }
        // this checks validity of recipient's account number and saves to "accountNumber" if valid
        if(sending){
            var valueIsValid:Boolean = false
            while(!valueIsValid) {
                print("> Recipient Account Number: ")
                accountNumber = readLine().toString()
                valueIsValid = isNotEmpty(accountNumber, "Please do not leave Account Number empty.")
                if(valueIsValid){
                    if (accountNumber == "C" || accountNumber == "c") {
                        sending = false
                    }
                }
            }
        }
        // this checks validity of amount to be sent and saves to "amount" if valid
        if(sending){
            var valueIsValid:Boolean = false
            while(!valueIsValid) {
                print("> Amount to send: ")
                amount = readLine().toString()
                valueIsValid = isNotEmpty(amount, "Please do not leave Account Number empty.")
                if(valueIsValid){
                    if (amount == "C" || amount == "c") {
                        sending = false
                    }
                }
                if(valueIsValid && sending){
                    valueIsValid = isNumber(amount, "Please make sure the amount is number.")
                }
                if(valueIsValid && sending){
                    valueIsValid = !amountIsLessOrZero(amount)
                }
                if(valueIsValid && sending){
                    valueIsValid = !checkIfAmountIsGreater(pin, amount.toDouble())
                }
            }
        }
        // this checks and compares data inputted and data on var "customerList" list
        if(sending){
            //?EVERYTHING SHOULD BE VALID
            //check if name and account number exists
            //check if remaining balance is enough to send money
            //check if account is the one that is logged in
            var recipientIsValid: Boolean = false
            var isSelf:Boolean = false
            for(customer in customerList){
                //check if customer exists with the name and accountnumber
                if(customer.name == name && customer.accountNumber == accountNumber){
                    recipientIsValid = true
                    //cannot send money to own account or logged in account lol
                    if(customer.pin == pin){
                        isSelf = true //set true to stop sending and deducting balance
                    }
                }
            }
            //sending and deducting block
            if(recipientIsValid && !isSelf){
                while(!sendIsValid){
                    println("Recipient: $name | Account Number: $accountNumber | Amount:  ₱$amount")
                    print("> Send now -> [P] Proceed | [C] Cancel: ")
                    val send = readLine().toString()
                    sendIsValid = isNotEmpty(send, "Please enter [Y] for Yes and [N] for No")
                    if(sendIsValid){
                        var money = amount.toDouble()
                        when(send){
                            "P" -> {
                                sendProceed(pin, money, name, accountNumber)
                            }
                            "p" -> {
                                sendProceed(pin, money, name, accountNumber)
                            }
                            "C" -> {
                                sendIsValid = true // to exit loop and back to sending fields
                            }
                            "c" -> {
                                sendIsValid = true // to exit loop and back to sending fields
                            }
                        }
                    }
                }
            }
            // prints prompt
            if(isSelf){
                println("Cannot send money to yourself.")
            }
            // another prompt
            if(!recipientIsValid){
                println("Recipient with this account number does not exist.")
            }
        }
    }
}

/*
====================================================VALIDATION====================================================
 */
// PIN VALIDITY FULL CHECK UP
fun checkPinValidity (pin:String):Boolean{
    //check if pin is not empty
    var isValid: Boolean = false;
    isValid = isFourCharactersLong(pin,"Pin code must be a 4 digit number.")
    if(isValid){
        isValid = isDigits(pin, "Please make sure all the characters are digit.")
    }
    return isValid
}
//GET CUSTOMER DATA TO CHECK IF IT REALLY EXIST
fun getCustomerData (pin: String): Customer{
    var result =  Customer("", "", "", 0.0)
    for(customer in customerList){
        if(customer.pin == pin){
            result = customer
        }
    }
    return result
}
//CHECK INPUT IF IT IS EMPTY
fun isNotEmpty(value: String, emptyPrompt:String):Boolean{
    if(value == ""
        || value.isNullOrBlank()
        || value.isEmpty() ){
        println(emptyPrompt)
        return false
    }
    return true
}
//CHECK INPUT IF IT IS A NUMBER
fun isNumber(value: String, invalidPrompt: String):Boolean{
    val numberRegex = "-?\\d+(\\.\\d+)?".toRegex()
    if(!value.matches(numberRegex)){
        println(invalidPrompt)
        return false;
    }
    return true;
}
//CHECK PIN INPUT IF IT IS FOUR CHARACTERS LONG
fun isFourCharactersLong(pin:String, invalidPrompt: String):Boolean{
    if(pin.length != 4){
        println(invalidPrompt)
        return false
    }
    return true
}
//CHECK PIN IF IT IS ALL DIGITS
fun isDigits(pin:String, invalidPrompt: String):Boolean{
    val pattern: Regex= "^[0-9]+$".toRegex()
    if(!pattern.matches(pin)){
        print(invalidPrompt)
        return false
    }
    return true
}
//CHECK AMOUNT VERSUS BALANCE OF THE CURRENT LOGGED IN
fun checkIfAmountIsGreater(pin:String, amount:Double):Boolean{
    for(customer in customerList){
        if(customer.pin == pin){
            if(amount > customer.balance){
                println("Amount is greater than your balance.")
                return true
            }
        }
    }
    return false
}

//RUNS IF PROCEED TO SENDING
fun sendProceed(pin:String, money: Double, name:String, accountNumber:String){
    for(customer in customerList){
        // deduct from sender
        if(customer.pin == pin){
            val newValue = customer.balance - money
            customerList[customerList.indexOf(customer)].balance = newValue
            println("You have sent ₱$money!!!\nYour current balance is: ₱${toCurrency(newValue.toString())}")
        }
        // add to recipient
        else if(customer.name == name && customer.accountNumber == accountNumber){
            val newValue = customer.balance + money
            customerList[customerList.indexOf(customer)].balance = newValue
        }
    }
    println("Successfully sent ₱$money to $name")
    sending = false
    sendIsValid = true
}

//for currency format
fun toCurrency(value: String): String {
    var formatter: NumberFormat = DecimalFormat("#,###.####")
    var myNumber = value.toDouble()
    return formatter.format(myNumber)
}

//CHECK IF AMOUNT IS LESS OR GREATER THAN 0
fun amountIsLessOrZero(value:String):Boolean{
    if(value.toDouble() <= 0){
        println("Amount cannot be less than or equal to zero.")
        return true
    }
    return false
}
/*
====================================================PRINT FUNCTIONS====================================================
                THIS ARE JUST FOR PRINTS NOTHING FABULOUS
 */
fun printIntro(){
    println("|============================================================================================|")
    println("|==================================WELCOME TO SIMPLE ATM!!===================================|")
    println("|--------------------------------------------------------------------------------------------|")
}
fun printOutro(){
    println("\n\n\n======================================================================")
    println("*-*-*-*-*-*-*-*-*-*-Thank you for using the system!-*-*-*-*-*-*-*-*-*-*")
    println("======================================================================")
}
fun printSystemStart(){
    println("\n=========================================[E]Exit System======================================");
    print("> Please enter your 4-digit pin: ")
}
fun printAuthenticated(name:String){
    println("=============================\t\t\tWelcome\t\t\t================================")
    println("============================\t\t\t$name\t\t\t=============================")
    println("-----------------------------------------------------------------------------------------")
}
fun printActions(){
    println("\n=========================[E] Exit System | [G] Go back to pin============================")
    println("================[B] Balance | [D] Deposit | [W] Withdraw | [S] Send Money================")
    print("> Please choose an action: ")
}
fun printMainFeatureTitle(action: String){
    println("\n========================================[C] Cancel========================================")
    println("------------------------------------ACTION: $action--------------------------------")
}

/*
====================================================DATA====================================================
 */

var customerList = listOf(
    Customer("Gark Godwin", "GG-0202-AA", "0000", 2000000.2332),
    Customer("John Paul", "GG-2312-BB","1111", 301020.23),
    Customer("Brixter", "GG-3214-CC", "2222", 150032.23),
    Customer("Leigh Anne","GG-0315-DD", "3333", 500300.23),
    Customer("Kenneth", "GG-2313-EE", "4444", 88888.23),
    Customer("Ruffa Mae", "GG-9832-FF", "5555", 191230.23),
    Customer("Francis Karl", "GG-1111-GG", "6666", 132800.23),
    Customer("Cene Cris", "GG-2222-HH", "7777", 1009199.23),
    Customer("Miguel Luigi", "GG-3333-II", "8888", 999320.23),
    Customer("Jaffe Lamae", "GG-4444-JJ", "9999", 9392887.23),
    )