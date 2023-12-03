import React from 'react';
import { useForm } from 'react-hook-form';

const keys: { value: string; type: 'text' | 'number' }[] = [
    { value: 'page', type: 'number' },
    { value: 'page_size', type: 'number' },
];

interface IDataType {
    page: number;
    page_size: number;
}

interface ISortByIdFormProps {
    submitHandler: (page: number | undefined, page_size: number | undefined) => void;
}

export const Pagination: React.FC<ISortByIdFormProps> = (props) => {
    const { register, handleSubmit } = useForm();
    const handleSortTicket = (data: IDataType) => {
        props.submitHandler(data.page, data.page_size);
    };

    return (
        <form
            className={'grid h-min w-60 grid-flow-row gap-2 rounded-2xl bg-light_purple p-5'}
            onSubmit={handleSubmit(handleSortTicket)}
        >
            {keys.map((x) => {
                return (
                    <input
                        className={'h-[26px] w-full rounded border bg-light_purple pl-2 placeholder-gray'}
                        key={x.value}
                        {...register(x.value)}
                        type={x.type}
                        placeholder={x.value}
                    />
                );
            })}
            <button type={'submit'} className={'mt-2 h-[26px] w-42 rounded bg-purple'}>
                Sort by page and size
            </button>
        </form>
    );
};
