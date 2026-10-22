# Activity Diagram - Apex ATM Banking System

This document illustrates the operational activity workflow for **ATM PIN Verification & Security Lockout**, followed by **Cash Dispense Processing**.

```mermaid
flowchart TD
    Start([Card Inserted]) --> InputPin[Prompt Customer for 4-Digit PIN]
    InputPin --> ReadDB[(Fetch Card from Database)]
    
    ReadDB --> CheckStatus{Is Card Active & Not Locked?}
    CheckStatus -- No (Status == LOCKED) --> DisplayLocked[Show Card Locked Warning • Advise OTP Reset / Branch Visit]
    DisplayLocked --> EjectCard([Eject ATM Card])

    CheckStatus -- Yes --> VerifyPinHash{Does Entered PIN Match Hash?}
    
    VerifyPinHash -- Yes --> ResetAttempts[Reset failed_pin_attempts = 0]
    ResetAttempts --> LogSuccess[Log SUCCESS in login_history]
    LogSuccess --> DisplayMenu[Display ATM Operation Menu]

    VerifyPinHash -- No --> IncAttempts[Increment failed_pin_attempts + 1]
    IncAttempts --> CheckThreshold{failed_pin_attempts >= 3?}
    
    CheckThreshold -- Yes --> LockCard[Set Card Status = 'LOCKED']
    LockCard --> LogLocked[Log ACCOUNT_LOCKED in login_history]
    LogLocked --> AlertLocked[Display: Maximum Attempts Exceeded. Card Locked for Security.]
    AlertLocked --> EjectCard

    CheckThreshold -- No --> CalcRemaining[Calculate Remaining Attempts: 3 - failed]
    CalcRemaining --> ShowWarning[Display: Incorrect PIN. Remaining Attempts: N]
    ShowWarning --> InputPin

    %% Post-Login Activity
    DisplayMenu --> SelectWithdrawal[Customer selects Cash Withdrawal]
    SelectWithdrawal --> EnterAmount[Enter Amount in Multiples of ₹100]
    
    EnterAmount --> CheckDailyLimit{Amount <= Remaining Daily Limit ₹40,000?}
    CheckDailyLimit -- No --> LimitError[Display DailyLimitExceededException]
    LimitError --> DisplayMenu

    CheckDailyLimit -- Yes --> CheckBalance{Account Polymorphic Check: Balance - Amount >= MinBalance?}
    CheckBalance -- No --> BalanceError[Display InsufficientFundsException]
    BalanceError --> DisplayMenu

    CheckBalance -- Yes --> DebitDB[(Atomically Deduct Balance & Record Transaction)]
    DebitDB --> DispenseCash[Activate Hardware Cash Dispenser Shutter]
    DispenseCash --> PrintReceipt[Generate Thermal Receipt Slip & PDF Advice]
    PrintReceipt --> Finish([Transaction Complete • Card Ejected])
```
