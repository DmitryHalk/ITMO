%xdefine prev 0

%macro colon 2
    %ifnstr %1
        %error "colon: первый аргумент (ключ) должен быть строкой в кавычках"
    %endif
    %ifid %2
    %else
        %error "colon: второй аргумент должен быть меткой"
    %endif

    %2:
        dq prev
        db %1, 0
    %xdefine prev %2
%endmacro

