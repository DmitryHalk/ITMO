print(f'Вариант: {502026%3}')
import re
def patttern(x):
    return fr"((({x})(-{x})?(/{x})?)(,{x}(-{x})?(/{x})?)*|\*|\*/{x})(,{x}(-{x})?(/{x})?)*"

min = '([0-9]|([1-5][0-9]))'
hour = '([01]?[0-9]|2[0-3])'
day = '([12]?[0-9]|3[01])'
month = '([0-9]|1[0-2])'
dayow = '([0-6])'
print('True' if re.fullmatch(fr"- {patttern(min)} {patttern(hour)} {patttern(day)} {patttern(month)} {patttern(dayow)}", input()) else "False")

