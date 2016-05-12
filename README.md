# BasketballTrainer
Application for a basketball trainer

This application was created for any basketball training company and intended to help trainers schedule their students.
The trainers will be able to add a new trainer if not already in the list, add a new student, student phone number, select a date
and time, a particular training drill, the facility the training will occur (facility option available), and lastly, add any notes
for the training. Once all information is correctly input, the trainer will be able to add or delete any scheduled sessions on the
list. All added sessions will be added into the SQL database.

Bugs:
  -Phone number will format only if 10 numbers are entered without any other symbols, otherwise if any 10 characters are
   entered, it will still be entered into the database.

Features:
  - "Add New Trainer" button will prompt to enter a new name and will be included in the list of trainers.
  - "Add New Facility" button will prompt to enter a new facility and will also be included in the list of trainers.
  - Within the list, "Student" column can be edited if the student's name is spelled incorrectly.

Future additions:
  - Set up table to filter out specific trainer's schedules
  - Set up training session history for all trainers
  - Create restrictions on scheduling dates and facilities
  - Seperating Date into 2 different JSpinners (Date and Time)
