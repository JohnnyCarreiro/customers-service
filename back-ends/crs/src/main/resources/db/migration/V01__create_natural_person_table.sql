/* V1__create_natural_person_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'NaturalPerson')
    BEGIN
        CREATE TABLE dbo.NaturalPerson (
           Id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
           Name NVARCHAR(100) NOT NULL,
           CPF CHAR(11) NOT NULL,
           CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
           UpdatedAt DATETIME2 NULL,
           DeletedAt DATETIME2 NULL
        );
        CREATE INDEX IX_NaturalPerson_Id ON dbo.NaturalPerson(Id)
        CREATE INDEX IX_NaturalPerson_CPF ON dbo.NaturalPerson(CPF)
    END