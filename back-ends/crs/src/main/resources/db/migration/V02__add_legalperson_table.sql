/* V2__add_legalperson_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'LegalPerson')
    BEGIN
        CREATE TABLE dbo.LegalPerson (
             Id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
             CorporateName NVARCHAR(100) NOT NULL,
             CNPJ CHAR(14) NOT NULL,
             CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
             UpdatedAt DATETIME2 NULL,
             DeletedAt DATETIME2 NULL
        );

        CREATE INDEX IX_Address_Id ON dbo.LegalPerson(Id)
        CREATE INDEX IX_Address_CEP ON dbo.LegalPerson(CNPJ)
        CREATE INDEX IX_Address_CustomerId ON dbo.LegalPerson(CorporateName)
    END