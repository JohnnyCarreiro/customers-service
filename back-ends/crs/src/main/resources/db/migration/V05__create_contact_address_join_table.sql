/* V3__create_address_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'ContactAddress')
    BEGIN
      CREATE TABLE ContactAddress (
        ContactId UNIQUEIDENTIFIER NOT NULL,
        AddressId UNIQUEIDENTIFIER NOT NULL,
        CONSTRAINT FK_ContactAddress_ContactId FOREIGN KEY (ContactId) REFERENCES Contact(Id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT FK_ContactAddress_AddressId FOREIGN KEY (AddressId) REFERENCES Address(Id)ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT PK_ContactAddress PRIMARY KEY (ContactId, AddressId)
     );
      CREATE INDEX IX_Address_Id ON dbo.ContactAddress(AddressId)
      CREATE INDEX IX_Contact_Id ON dbo.ContactAddress(ContactId)
    END
