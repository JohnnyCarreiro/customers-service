/* V06__create_natural_person_contact_join_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'NaturalPersonContact')
    BEGIN
      CREATE TABLE NaturalPersonContact (
        ContactId UNIQUEIDENTIFIER NOT NULL,
        CustomerId UNIQUEIDENTIFIER NOT NULL,
        CONSTRAINT FK_NaturalPersonContact_ContactId FOREIGN KEY (ContactId) REFERENCES Contact(Id),
        CONSTRAINT FK_NaturalPersonContact_CustomerId FOREIGN KEY (CustomerID) REFERENCES NaturalPerson(Id),
        CONSTRAINT PK_NaturalPersonContact PRIMARY KEY (ContactId, CustomerID)
     );
      CREATE INDEX IX_Contact_Id ON dbo.NaturalPersonContact(ContactId)
      CREATE INDEX IX_Customer_Id ON dbo.NaturalPersonContact(CustomerID)
    END
