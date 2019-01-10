INSERT INTO I18N (KEY, FINNISH, SWEDISH, ENGLISH)
values ('item_cents', 'senttiä', 'cent', 'cents');

select * from I18N;

insert into ITEM (ID_I18N, PRICE)
values (13, 1);
