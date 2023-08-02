/* V3__create_address_table.sql */

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Address')
    BEGIN
        CREATE TABLE dbo.[Address] (
             Id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWSEQUENTIALID(),
             Street NVARCHAR(100) NOT NULL,
             Number INT NOT NULL,
             Complement NVARCHAR(100) NULL,
             Area NVARCHAR(100) NOT NULL,
             City NVARCHAR(100) NOT NULL,
             State CHAR(2) NOT NULL,
             CEP CHAR(9) NOT NULL,
             UnitType NVARCHAR(100) NOT NULL,
             CustomerId UNIQUEIDENTIFIER NOT NULL,
             CreatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
             UpdatedAt DATETIME2 NOT NULL DEFAULT GETUTCDATE(),
             DeletedAt DATETIME2 NULL
        );

        CREATE INDEX IX_Address_Id ON dbo.Address(Id)
        CREATE INDEX IX_Address_Street ON dbo.Address(Street)
        CREATE INDEX IX_Address_Number ON dbo.Address(Number)
        CREATE INDEX IX_Address_CEP ON dbo.Address(CEP)
        CREATE INDEX IX_Address_CustomerId ON dbo.Address(CustomerId)
    END