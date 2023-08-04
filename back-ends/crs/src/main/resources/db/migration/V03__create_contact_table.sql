/* V3__create_address_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Address')
    BEGIN
        CREATE TABLE dbo.[Contact] (
             Id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
             PhoneNumber NVARCHAR(11) NOT NULL,
             Email NVARCHAR(100) NOT NULL UNIQUE,
             CustomerId UNIQUEIDENTIFIER NOT NULL UNIQUE,
             CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
             UpdatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
             DeletedAt DATETIME2 NULL
        );

        CREATE INDEX IX_Contact_Id ON dbo.Contact(Id)
        CREATE INDEX IX_Contact_Phone_Number ON dbo.Contact(PhoneNumber)
        CREATE INDEX IX_Contact_Email ON dbo.Contact(Email)
    END
