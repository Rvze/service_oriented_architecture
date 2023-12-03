import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useDeletePersonMutation, useDeleteTicketMutation } from '../../../core/api/ticket';

const keys: { value: string; type: 'text' | 'number' }[] = [{ value: 'id', type: 'number' }];

interface IDataType {
    id: number;
}

export const DeletePersonForm: React.FC = () => {
    const { register, handleSubmit } = useForm();
    const [deletePerson] = useDeletePersonMutation();
    const [error, setError] = useState('');
    const handleDeletePerson = async (data: IDataType) => {
        await deletePerson(data.id)
            .unwrap()
            .then(() => setError(''))
            .catch(() => setError('server error'));
    };

    return (
        <form
            className={'grid h-min w-60 grid-flow-row gap-2 rounded-2xl bg-light_purple p-5'}
            onSubmit={handleSubmit(handleDeletePerson)}
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
            <button type={'submit'} className={'mt-2 h-[26px] w-28 rounded bg-purple'}>
                Delete Person
            </button>
            <div className={'flex h-[30px] items-center justify-center text-red'}>{error}</div>
        </form>
    );
};
