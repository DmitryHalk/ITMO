import polars as pl
print(f'Вариант: {26%40}')

pl.Config.set_tbl_width_chars(500)      # ширина таблицы
pl.Config.set_tbl_cols(-1)              # все столбцы (-1 = без ограничений)
pl.Config.set_tbl_rows(12)


pl.Config.set_tbl_hide_column_names(True)      # убрать имена столбцов
pl.Config.set_tbl_hide_column_data_types(True)
pl.Config.set_tbl_hide_dataframe_shape(True)

df = pl.read_excel('lab5.xlsx', sheet_name='Лист1', infer_schema_length=0)#Не определять типы столбцов)
print(df[2:14, :25].drop('__UNNAMED__5'))


